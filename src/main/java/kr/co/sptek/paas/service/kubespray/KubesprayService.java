package kr.co.sptek.paas.service.kubespray;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.co.sptek.paas.model.ClusterInfo;
import kr.co.sptek.paas.model.NodeCheckResult;
import kr.co.sptek.paas.model.ProcessResult;
import kr.co.sptek.paas.service.ConfigurationService;
import kr.co.sptek.paas.service.cluster.IClusterService;
import kr.co.sptek.paas.utils.CompressUtil;
import kr.co.sptek.paas.utils.FileUtils;

@Service
public class KubesprayService implements IClusterService {	
	private static Logger logger = LoggerFactory.getLogger(KubesprayService.class);
	
	private static final int CREATE = 0;
	private static final int SCALE = 1;
	private static final int DELETE = 2;
	
	private ConfigurationService configService;
	private ExecProcessAsync asyncService;
	
	public KubesprayService(ConfigurationService configService) {
		this.configService = configService;
		this.asyncService = new ExecProcessAsync();
	}
	
	
	@Override
	public ProcessResult createCluster(ClusterInfo clusterInfo) {
		logger.info("Kubespray - Cluster 생성 시작. - {}", clusterInfo.getClusterName());
		return kubesprayJob(clusterInfo, CREATE);
	}
	
	@Override
	public ProcessResult deleteCluster(ClusterInfo clusterInfo) {
		logger.info("Kubespray - Cluster 삭제 시작. - {}", clusterInfo.getClusterName());
		return kubesprayJob(clusterInfo, DELETE);
	}


	@Override
	public ProcessResult updateScale(ClusterInfo clusterInfo) {
		logger.info("Kubespray - Cluster scale 업데이트 시작. - {}", clusterInfo.getClusterName());
		return kubesprayJob(clusterInfo, SCALE);
	}
	
	private ProcessResult kubesprayJob(ClusterInfo clusterInfo, int jobType) {
		logger.info("kubespray cluster job start.");
		
		String kubesprayBinHome = configService.getKubesprayBinHome();
		File kubesprayHome = new File(kubesprayBinHome);
		boolean isReady = kubesprayHome.isDirectory();
		if(!isReady) {
			logger.info("kubespray not installed.");
			
			String kubesprayVersion = configService.getKubesprayVersion();
			try {
				isReady = installKubespray(kubesprayVersion);
			} catch (IOException e) {
				logger.error("Kubespray install fail.");
				logger.error("", e);
				return ProcessResult.errorMessage("Kubespray install fail.");
			}
		}
		
		if(!isReady) {
			logger.error("Kubespray cluster job fail - kubespray가 설치 되지 않았습니다.");
			return ProcessResult.errorMessage("Kubespray cluster job fail - kubespray가 설치 되지 않았습니다.");
		}
		
		InventoryGenerator generator = new InventoryGenerator();
		String clusterName = clusterInfo.getClusterName();
		String inventoryContents = generator.genInventory(clusterInfo);
		String inventoryHome = configService.getKubesprayInventoryHome();
		File inventoryDir = new File(inventoryHome, clusterName);
		File inventoryFile = new File(inventoryDir, "inventory.ini");
		
		try {
			logger.info("inventory.ini 생성 시작");
			FileUtils.fileWrite(inventoryFile, inventoryContents);
			logger.info("inventory.ini 생성 종료");
		} catch (IOException e) {
			logger.error("inventory.ini 생성 실패");
			logger.error("", e);
			return ProcessResult.errorMessage("inventory.ini 생성 실패.");
		}
		
		/*
		//핑테스트.
		ProcessResult pingResult = pingTest(kubesprayBinHome, inventoryFile);
		if(pingResult.getExitCode() != 0) {
			return pingResult;
		}
		*/
		
		//클러스터 생성 커맨드 실행
		String command = null;
		
		
		switch (jobType) {
		case CREATE:
			command = genCreateClusterCommand(inventoryFile.getAbsolutePath(), clusterInfo.getUserName());
			break;
		case SCALE:
			command = genClusterScaleCommand(inventoryFile.getAbsolutePath(), clusterInfo.getUserName());
			break;
		case DELETE:
			command = genClusterResetCommand(inventoryFile.getAbsolutePath(), clusterInfo.getUserName());
			break;
		}
		
		System.out.println(command);
		
		
		logger.info("cluster process start.");
		logger.info("cluster process command: {}", command);
		
		//registerListener(System.out::println);
		int exitCode = -1;
		try {
			exitCode = asyncService.process(command, kubesprayBinHome);
		} catch (IOException e) {
			logger.error("", e);
			
		}
		
		if(exitCode != 0) {
			return ProcessResult.errorMessage("Cluster job 프로세스 실행 실패.");
		}
		return ProcessResult.successMessage();
	}
	
	/**
	 * 노드 확인을 위해 핑 테스트
	 * @return
	 */
	public ProcessResult pingTest(String kubesprayBinHome, File inventoryFile) {
		String pingCommnad = genPingCommand(inventoryFile.getAbsolutePath());
		logger.info("Ping test start.");
		logger.info("Ping command: {}", pingCommnad);
		
		ProcessResult result = null;
		try {
			ExecProcess processService = new ExecProcess();
			result = processService.process(pingCommnad, kubesprayBinHome, new String[] {"[WARNING]:"});
		} catch (IOException e) {
			logger.error("Node ping 테스트 실패");
			logger.error("", e);
			return ProcessResult.errorMessage("Node ping 테스트 실패.");
		}
		logger.info("Ping test result - exit code: {}", result.getExitCode());
		logger.info("Ping test result - message: {}", result.getMessage());
		
		
		if(result.getExitCode() != 0) {
			logger.info("Ping test error.");
			return ProcessResult.errorMessage("Ping test error.");
		}
		
		
		List<NodeCheckResult> resultList = parserPingTest(result);
		for(NodeCheckResult r : resultList) {
			if(!r.isStatus()) {
				//하나의 노드라도 준비가 안되있으면 중지				
				return ProcessResult.errorMessage(String.format("Node가 준비되지 않았습니다. %s - %s", r.getNodeName(), r.getMessage()));
			}
		}
		return ProcessResult.successMessage();
	}
	
	

	
	
	private List<NodeCheckResult> parserPingTest(ProcessResult result) {
		String message = result.getMessage();
		List<NodeCheckResult> list = new ArrayList<>();
		int index = 0;           
		while(true) {
			
			//시작 인덱스
			int p = message.indexOf("=>", index);
			if(p < 0) {
				break;
			}			
			
			//종료 인덱스
			int n = message.indexOf("=>", p + 2);
			if(n < 0) {
				n = message.length();
			}
			String sub = message.substring(index, n);			
			int e = sub.lastIndexOf("}") + 1;
			
			
			String nodeBody = sub.substring(0, e);
			
			String[] sp = nodeBody.split("=>");
			if(sp.length == 2) {
				String nodeInfo = sp[0].replaceAll("\\s+","");
				String details = sp[1];
				//System.out.println(details);
				
				
				String[] nodeArr = nodeInfo.split("\\|");
				String nodeName = nodeArr[0];
				//String nodeStatus = nodeArr[1];
				
				
				JsonObject jsonObject = new Gson().fromJson(details, JsonObject.class);
				JsonElement pingElement = jsonObject.get("ping");
				boolean ping = false;
				String msg = null;
				if(pingElement != null) {
					String pingResult = pingElement.getAsString();
					ping = pingResult.equals("pong");
				} else {
					JsonElement msgElement = jsonObject.get("msg");
					if(msgElement != null) {
						//핑 테스트 에러인 경우 에러 메시지.
						msg = msgElement.getAsString();
					}
				}
				
				
				NodeCheckResult checkResult = new NodeCheckResult();
				checkResult.setNodeName(nodeName);
				checkResult.setStatus(ping);
				checkResult.setMessage(msg);
				list.add(checkResult);
			}
			
			index = index + nodeBody.length();
		}
		
		return list;
	}
	
	
	
	
	/**
	 * Inventory Ping command
	 * @param inventoryFilePath
	 * @return
	 */
	private String genPingCommand(String inventoryFilePath) {
		return String.format("ansible all -i %s -m ping", inventoryFilePath);
	}
	
	/**
	 * Create Cluster Command
	 * @param inventoryFilePath
	 * @param userName
	 * @return
	 */
	private String genCreateClusterCommand(String inventoryFilePath, String userName) {
		return String.format("ansible-playbook -i %s -become --become-user=%s cluster.yml", inventoryFilePath, userName);
	}
	
	
	/**
	 * Cluster Scale 조정 커맨드
	 * @param inventoryFilePath
	 * @param userName
	 * @return
	 */
	private String genClusterScaleCommand(String inventoryFilePath, String userName) {
		return String.format("ansible-playbook -i %s -become --become-user=%s scale.yml", inventoryFilePath, userName);
	}
	
	/**
	 * Cluster Reset 커맨드
	 * @param inventoryFilePath
	 * @param userName
	 * @return
	 */
	private String genClusterResetCommand(String inventoryFilePath, String userName) {
		return String.format("ansible-playbook -i %s -become --become-user=%s reset.yml --extra-vars \"reset_confirmation=yes\"", inventoryFilePath, userName);
	}
	
	
	/**
	 * kubespray 설치
	 * @return
	 */
	public boolean installKubespray(String version) throws IOException {
		logger.info("kubespray install start");
		String zipFileName = "kubespray.zip";
		String path = String.format("static/kubespray/%s/%s", version, zipFileName); 
		ClassPathResource resource = new ClassPathResource(path);
		InputStream is = resource.getInputStream();
		
		String kubesprayHome = configService.getKubesprayBinHome();
		if(is != null) {
			File zipFile = new File(kubesprayHome, zipFileName);
			try {
				FileUtils.copy(is, zipFile);
			} catch (IOException e) {
				logger.error("kubespray install fail - 파일 복사 실패. target path:{}", zipFile.getAbsolutePath());
				logger.error("", e);
				return false;
			}
			
			
			CompressUtil compressUtil = new CompressUtil();
			try {
				compressUtil.unzip(zipFile);
			} catch (IOException e) {
				logger.error("kubespray install fail - 압축 해제 실패. target path:{}", zipFile.getAbsolutePath());
				logger.error("", e);
				return false;
			}
			zipFile.delete();
		}
		
		logger.info("kubespray install success");
		return true;
	}
	
	/**
	 * kubespray 버전 반환.
	 * @return
	 * @throws IOException
	 */
	public String[] getKubesprayVersion() throws IOException {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:/static/kubespray/*");
		List<String> list = new ArrayList<>();
		if(resources != null) {
			for(Resource r : resources) {
				String dirName = r.getFilename();
				list.add(dirName);
			}
		}
		return list.toArray(new String[list.size()]);
	}


	@Override
	public void registerListener(Consumer<String> consumer) {
		this.asyncService.register(consumer);
	}
	
}
 
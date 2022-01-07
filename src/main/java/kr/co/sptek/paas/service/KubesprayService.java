package kr.co.sptek.paas.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import kr.co.sptek.paas.model.Cluster;
import kr.co.sptek.paas.model.ProcessResult;
import kr.co.sptek.paas.utils.CompressUtil;
import kr.co.sptek.paas.utils.FileUtils;

@Service
public class KubesprayService {	
	private static Logger logger = LoggerFactory.getLogger(KubesprayService.class);
	
	@Autowired
	private ConfigurationService configService;
	
	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private ProcessService processService;

	public boolean createCluster(Cluster cluster) throws IOException {
		logger.info("kubespray cluster creation start.");
		
		String kubesprayBinHome = configService.getKubesprayBinHome();
		File kubesprayHome = new File(kubesprayBinHome);
		boolean isReady = kubesprayHome.isDirectory();
		if(!isReady) {
			logger.info("kubespray not installed.");
			
			String kubesprayVersion = configService.getKubesprayVersion();
			isReady = installKubespray(kubesprayVersion);
		}
		
		if(!isReady) {
			logger.error("Kubespray cluster creation fail - kubespray가 설치되어 있지 않습니다.");
			return false;
		}
		
		String clusterName = cluster.getName();
		String inventoryContents = inventoryService.genInventory(cluster);
		String inventoryHome = configService.getKubesprayInventoryHome();
		File inventoryDir = new File(inventoryHome, clusterName);
		File inventoryFile = new File(inventoryDir, "inventory.ini");
		
		FileUtils.fileWrite(inventoryFile, inventoryContents);
		
		String pingCommnad = genPingCommand(inventoryFile.getAbsolutePath());
		logger.info("Ping test start.");
		logger.info("Ping command: {}", pingCommnad);
		
		ProcessResult result = processService.process(pingCommnad, kubesprayBinHome);
		logger.info("Ping test result - exit code: {}", result.getExitCode());
		logger.info("Ping test result - message: {}", result.getMessage());
		
		
		if(result.getExitCode() != 0) {
			logger.info("Ping test error.");
			return false;
		}
		
		
		
		
		return true;
	}
	
	/**
	 * Inventory Ping command 생성하여 반환.
	 * @param inventoryFilePath
	 * @return
	 */
	private String genPingCommand(String inventoryFilePath) {
		return String.format("ansible all -i %s -m ping", inventoryFilePath);
	}
	
	
	
	/**
	 * kubespray가 설치 되어 있지 않을 경우 설치
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
				logger.error("kubespray install fail - 파일 복사 오류. target path:{}", zipFile.getAbsolutePath());
				logger.error("", e);
				return false;
			}
			
			
			CompressUtil compressUtil = new CompressUtil();
			try {
				compressUtil.unzip(zipFile);
			} catch (IOException e) {
				logger.error("kubespray install fail - 압축 파일 해제 오류. target path:{}", zipFile.getAbsolutePath());
				logger.error("", e);
				return false;
			}
			zipFile.delete();
		}
		
		logger.info("kubespray install success");
		return true;
	}
	
	/**
	 * kubespray 버전 리턴.
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
	
}
 
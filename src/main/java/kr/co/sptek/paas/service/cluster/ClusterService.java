package kr.co.sptek.paas.service.cluster;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sptek.paas.model.CreateClusterInfo;
import kr.co.sptek.paas.model.ProcessResult;
import kr.co.sptek.paas.service.ConfigurationService;
import kr.co.sptek.paas.service.kubespray.KubesprayService;

@Service
public class ClusterService {
	public static Set<String> SUPPORTED_CLUSTER_API = new HashSet<>(Arrays.asList("Alibaba Cloud", 
			"AWS", "Azure", "Azure Stack HCI", "Baidu Cloud", "BYOH", "Metal3", "DigitalOcean", "Exoscale", "GCP"
			,"IBM Cloud", "MAAS", "Nested", "OpenStack", "Equinix Metal", "Sidero", "Tencent Cloud", "vSphere"));
	
	private static final int CREATE = 0;
	private static final int SCALE = 1;
	private static final int DELETE = 2;

	
	private static Map<String, IClusterService> clusterProcessMap;
	
	
	

	@Autowired
	private ConfigurationService configService;
	
	/**
	 * 클러스터 생성.
	 * @param clusterInfo
	 * @return
	 */
	public ProcessResult createCluster(CreateClusterInfo clusterInfo) {
		return clusterJob(clusterInfo, CREATE);
	}
	
	/**
	 * 클러스터 scale update.
	 * @param clusterInfo
	 * @return
	 */
	public ProcessResult updateScale(CreateClusterInfo clusterInfo) {
		return clusterJob(clusterInfo, SCALE);
	}
	
	/**
	 * 클러스터 생성, 스케일 업데이트 작업 수행.
	 * @param clusterInfo
	 * @param jobType
	 * @return
	 */
	private ProcessResult clusterJob(CreateClusterInfo clusterInfo, int jobType) {
		String clusterName = clusterInfo.getClusterName();
		String provider = clusterInfo.getProvider();
		
		IClusterService creater = getClusterService(provider);
		wait(clusterName);
		
		getClusterProcessMap().put(clusterName, creater);
		
		
		ProcessResult result = null;
		switch (jobType) {
		case CREATE:
			result = creater.createCluster(clusterInfo);
			break;
		case SCALE:
			result = creater.updateScale(clusterInfo);
			break;
		}
        getClusterProcessMap().remove(clusterName);
		return result;
	}
	
	/**
	 * Provider에 따른 Cluster 서비스 리턴.
	 * @param provider
	 * @return
	 */
	private IClusterService getClusterService(String provider) {
		IClusterService creater = null;
		if(isSupportedClusterAPI(provider)) {
			//Cluster API 연동 클래스 등록.
		} else {
			creater = new KubesprayService(configService);
		}
		return creater;
	}
	
	/**
	 * 클러스터 삭제.
	 * @param clusterName
	 * @return
	 */
	public ProcessResult deleteCluster(String clusterName) {
		return null;
	}
	
	
	/**
	 * 콘솔 로그 리스너 등록.
	 * @param clusterName
	 * @param listener
	 * @return
	 */
	public boolean registryClusterJobLog(String clusterName, Consumer<String> listener) {
		Map<String, IClusterService> map = getClusterProcessMap();
		IClusterService service = map.get(clusterName);
		if(service != null) {
			service.registerListener(listener);
			return true;
		}
		return false;		
	}
	
	/**
	 * 작업하려는 클러스터가 이미 작업중일 경우 이전 작업이 종료 될때까지 기다린다.
	 * @param clusterName
	 */
	private void wait(String clusterName) {
		while(true) {
			if(getClusterProcessMap().containsKey(clusterName)) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				break;
			}
		}
	}
	
	public static Map<String, IClusterService> getClusterProcessMap() {
		if(clusterProcessMap == null) {
			clusterProcessMap = Collections.synchronizedMap(new HashMap<String, IClusterService>());
		}
		return clusterProcessMap;
	}
	
	private boolean isSupportedClusterAPI(String provider) {
		return SUPPORTED_CLUSTER_API.contains(provider);
	}
	
}

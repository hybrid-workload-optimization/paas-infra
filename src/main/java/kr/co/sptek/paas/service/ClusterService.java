package kr.co.sptek.paas.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sptek.paas.model.CreateClusterInfo;

@Service
public class ClusterService {
	public static Set<String> SUPPORTED_CLUSTER_API = new HashSet<>(Arrays.asList("Alibaba Cloud", 
			"AWS", "Azure", "Azure Stack HCI", "Baidu Cloud", "BYOH", "Metal3", "DigitalOcean", "Exoscale", "GCP"
			,"IBM Cloud", "MAAS", "Nested", "OpenStack", "Equinix Metal", "Sidero", "Tencent Cloud", "vSphere"));

	
	@Autowired
	private ConfigurationService configService;
	
	
	public boolean createCluster(CreateClusterInfo clusterInfo) {
		IClusterService creater = getClusterCreater(clusterInfo.getProvider());
		return creater.createCluster(clusterInfo);
	}
	
	private IClusterService getClusterCreater(String provider) {
		IClusterService creater = null;
		if(SUPPORTED_CLUSTER_API.contains(provider)) {
			
		} else {
			creater = new KubesprayService(configService);
		}
		return creater;
	}
}

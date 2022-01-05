package kr.co.sptek.paas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sptek.paas.model.Cluster;

@Service
public class KubesprayService {
	
	@Autowired
	ConfigurationService configService;

	public boolean createCluster(Cluster cluster) {
		return true;
	}
	
	/**
	 * kubespray가 설치 되어 있지 않을 경우 설치
	 * @return
	 */
	public boolean installKubespray() {
		String kubesprayHome = configService.getKubesprayHome();
		
		return true;
	}
	
}

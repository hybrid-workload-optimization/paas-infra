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
	 * kubespray�� ��ġ �Ǿ� ���� ���� ��� ��ġ
	 * @return
	 */
	public boolean installKubespray() {
		String kubesprayHome = configService.getKubesprayHome();
		
		return true;
	}
	
}

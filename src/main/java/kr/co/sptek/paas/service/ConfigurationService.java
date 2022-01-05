package kr.co.sptek.paas.service;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

	/**
	 * Portal home dir ��� ����.
	 * @return
	 */
	public String getHome() {
		return "D:/paas-portal";
	}
	
	/**
	 * Kubespray home ��� ����.
	 * @return
	 */
	public String getKubesprayHome() {
		String home = getHome();
		if(!home.endsWith("/")) {
			home+="/";
		}
		return home + "kubespray";
	}
}

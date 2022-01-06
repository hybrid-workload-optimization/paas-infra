package kr.co.sptek.paas.service;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {
	public static final String KUBESPRAY_DIR_NAME = "kubespray";
	public static final String KUBESPRAY_INVENTORY_DIR_NAME = "inventory";

	/**
	 * Portal home dir 경로 리턴.
	 * @return
	 */
	public String getHome() {
		String home = "D:/paas-portal";
		if(!home.endsWith("/")) {
			home+="/";
		}
		return home;
	}
	
	/**
	 * Kubespray 바이너리 실행 경로 리턴.
	 * @return
	 */
	public String getKubesprayBinHome() {
		String version = getKubesprayVersion();
		String binHome = String.format("%s%s/", getKubesprayHome(), version);
		return binHome;
	}
	
	/**
	 * Kubespray 바이너리 실행 경로 리턴.
	 * @return
	 */
	public String getKubesprayInventoryHome() {
		String inventoryHome = String.format("%s%s/", getKubesprayHome(), KUBESPRAY_INVENTORY_DIR_NAME);
		return inventoryHome;
	}
	
	/**
	 * Kubespray home 경로 리턴.
	 * @return
	 */
	public String getKubesprayHome() {
		return String.format("%s%s/", getHome(), KUBESPRAY_DIR_NAME);
	}
	
	/**
	 * 사용할 kubespray version 리턴.
	 * @return
	 */
	public String getKubesprayVersion() {
		return "2.10";
	}
}

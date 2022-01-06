package kr.co.sptek.paas.service;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {
	public static final String KUBESPRAY_DIR_NAME = "kubespray";
	public static final String KUBESPRAY_INVENTORY_DIR_NAME = "inventory";

	/**
	 * Portal home dir ��� ����.
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
	 * Kubespray ���̳ʸ� ���� ��� ����.
	 * @return
	 */
	public String getKubesprayBinHome() {
		String version = getKubesprayVersion();
		String binHome = String.format("%s%s/", getKubesprayHome(), version);
		return binHome;
	}
	
	/**
	 * Kubespray ���̳ʸ� ���� ��� ����.
	 * @return
	 */
	public String getKubesprayInventoryHome() {
		String inventoryHome = String.format("%s%s/", getKubesprayHome(), KUBESPRAY_INVENTORY_DIR_NAME);
		return inventoryHome;
	}
	
	/**
	 * Kubespray home ��� ����.
	 * @return
	 */
	public String getKubesprayHome() {
		return String.format("%s%s/", getHome(), KUBESPRAY_DIR_NAME);
	}
	
	/**
	 * ����� kubespray version ����.
	 * @return
	 */
	public String getKubesprayVersion() {
		return "2.10";
	}
}

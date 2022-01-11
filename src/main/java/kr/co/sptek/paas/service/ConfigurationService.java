package kr.co.sptek.paas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {
	public static final String KUBESPRAY_DIR_NAME = "kubespray";
	public static final String KUBESPRAY_INVENTORY_DIR_NAME = "inventory";
	
	@Value("${app.home}")
	private String HOME_DIR;

	/**
	 * Portal home dir 반환.
	 * @return
	 */
	public String getHome() {
		String home = HOME_DIR;
		if(!home.endsWith("/")) {
			home+="/";
		}
		return home;
	}
	
	/**
	 * Kubespray 실행 폴더 리턴.
	 * @return
	 */
	public String getKubesprayBinHome() {
		String version = getKubesprayVersion();
		String binHome = String.format("%s%s/", getKubesprayHome(), version);
		return binHome;
	}
	
	/**
	 * Kubespray 인벤토리 폴더 반환.
	 * @return
	 */
	public String getKubesprayInventoryHome() {
		String inventoryHome = String.format("%s%s/", getKubesprayHome(), KUBESPRAY_INVENTORY_DIR_NAME);
		return inventoryHome;
	}
	
	/**
	 * Kubespray home 경로 반환.
	 * @return
	 */
	public String getKubesprayHome() {
		return String.format("%s%s/", getHome(), KUBESPRAY_DIR_NAME);
	}
	
	/**
	 * 설정된 kubespray version 리턴.
	 * @return
	 */
	public String getKubesprayVersion() {
		return "2.10";
	}
}

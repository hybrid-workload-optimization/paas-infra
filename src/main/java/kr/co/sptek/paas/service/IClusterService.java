package kr.co.sptek.paas.service;

import kr.co.sptek.paas.model.CreateClusterInfo;

public interface IClusterService {
	
	/**
	 * 클러스터 생성.
	 * @param cluster
	 * @return
	 */
	public boolean createCluster(CreateClusterInfo clusterInfo);
}

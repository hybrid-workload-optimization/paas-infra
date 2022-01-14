package kr.co.sptek.paas.service.cluster;

import java.util.function.Consumer;

import kr.co.sptek.paas.model.ClusterInfo;
import kr.co.sptek.paas.model.ProcessResult;

public interface IClusterService {
	
	/**
	 * 클러스터 생성.
	 * @param cluster
	 * @return
	 */
	public ProcessResult createCluster(ClusterInfo clusterInfo);
	
	/**
	 * 클러스터 삭제.
	 * @return
	 */
	public ProcessResult deleteCluster(ClusterInfo clusterInfo);
	
	
	/**
	 * 클러스터 스케일 조정.
	 * @return
	 */
	public ProcessResult updateScale(ClusterInfo clusterInfo);
	
	
	
	/**
	 * 로그 컨슈머 등록
	 * @param listener
	 */
	public void registerListener(Consumer<String> listener);
}

package kr.co.sptek.paas.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("k8s Cluster 정보")
public class Cluster {

	@ApiModelProperty(value="clusterName", required=true)
	private String clusterName;	
	
	@ApiModelProperty(value="nodes", required=true)
	private Node[] nodes;
	
	@ApiModelProperty(value="provider", required=true)
	private String provider;	
	
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public Node[] getNodes() {
		return nodes;
	}
	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
}

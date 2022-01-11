package kr.co.sptek.paas.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("k8s Cluster 정보")
public class Cluster {

	@ApiModelProperty(value="name", required=true)
	private String name;	
	
	@ApiModelProperty(value="nodes", required=true)
	private Node[] nodes;
	
	@ApiModelProperty(value="provider", required=true)
	private String provider;	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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

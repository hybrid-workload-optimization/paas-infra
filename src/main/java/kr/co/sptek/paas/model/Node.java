package kr.co.sptek.paas.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Node Á¤º¸")
public class Node {
	
	@ApiModelProperty(value="name", required=true)
	private String name;	

	@ApiModelProperty(value="ip", required=true)
	private String ip;
	
	@ApiModelProperty(value="nodeType", required=true)
	private NodeType nodeType;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}
}

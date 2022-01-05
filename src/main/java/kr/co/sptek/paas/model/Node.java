package kr.co.sptek.paas.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Node Á¤º¸")
public class Node {
	
	@ApiModelProperty(value="name", required=true)
	private String name;	

	@ApiModelProperty(value="ip", required=true)
	private String ip;
	
	@ApiModelProperty(value="nodeTypes", required=true)
	private List<NodeType> nodeTypes;
	

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
	
	public List<NodeType> getNodeTypes() {
		return nodeTypes;
	}

	public void setNodeTypes(List<NodeType> nodeTypes) {
		this.nodeTypes = nodeTypes;
	}
	
}

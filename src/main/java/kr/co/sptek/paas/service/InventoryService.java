package kr.co.sptek.paas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sptek.paas.model.Cluster;
import kr.co.sptek.paas.model.Node;
import kr.co.sptek.paas.model.NodeType;

@Service
public class InventoryService {

	public String genInventory(Cluster cluster) {
		
		StringBuffer allSection = new StringBuffer();
		StringBuffer kubeControlSection = new StringBuffer();
		StringBuffer etcdSection = new StringBuffer();
		StringBuffer kubeNodeSection = new StringBuffer();
		StringBuffer calicoSection = new StringBuffer();
		
		append(allSection, "[all]");
		append(kubeControlSection, "[kube_control_plane]");
		append(etcdSection, "[etcd]");
		append(kubeNodeSection, "[kube_node]");
		append(calicoSection, "[calico_rr]");
		
		
		Node[] nodes = cluster.getNodes();
		for(int i=0; i<nodes.length; i++) {
			Node node = nodes[i];
			String host = node.getName();
			String etcdName = "etcd" + i+1;
			String ip = node.getIp();
			List<NodeType> types = node.getNodeTypes();
			
			
			String allString = String.format("%s ansible_host=%s  ip=%s etcd_member_name=%s", host, ip, ip, etcdName);
			append(allSection, allString);
			
			if(types.contains(NodeType.master)) {
				append(kubeControlSection, host);
				append(etcdSection, host);
			}
			
			if(types.contains(NodeType.worker)) {
				append(kubeNodeSection, host);
			}
			
			if(types.contains(NodeType.network)) {
				append(calicoSection, host);
			}
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(allSection);
		sb.append(kubeControlSection);
		sb.append(etcdSection);
		sb.append(kubeNodeSection);
		sb.append(calicoSection);
		append(sb, "[k8s_cluster:children]");
		append(sb, "kube_control_plane");
		append(sb, "kube_node");
		append(sb, "calico_rr");
		
		return sb.toString();
	}
	
	private void append(StringBuffer sb, String str) {
		sb.append(str);
		sb.append("\n");
	}
	
}

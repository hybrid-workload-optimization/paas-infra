package kr.co.sptek.paas.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.sptek.paas.model.CreateClusterInfo;
import kr.co.sptek.paas.service.ClusterService;

@RestController
@Api(tags = {"Cluster"})
@RequestMapping("/cluster")
public class ClusterController {
	
	@Autowired
	private ClusterService clusterService;

	@ApiOperation(value="Cluster 생성",
	notes=""
			+"***입력부***\n"
			+"```\n"
			+"{\r\n"
			+ "  \"name\": \"cluster1\",\r\n"
			+ "  \"provider\": \"private\",\r\n"
			+ "  \"userName\": \"root\",\r\n"
			+ "  \"nodes\": [\r\n"
			+ "    {\r\n"
			+ "      \"name\": \"node1\",\r\n"
			+ "      \"ip\": \"172.16.10.115\",\r\n"
			+ "      \"nodeTypes\": [\r\n"
			+ "        \"master\",\r\n"
			+ "        \"network\"\r\n"
			+ "      ]\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "      \"name\": \"node2\",\r\n"
			+ "      \"ip\": \"172.16.10.116\",\r\n"
			+ "      \"nodeTypes\": [\r\n"
			+ "        \"master\",\r\n"
			+ "        \"network\"\r\n"
			+ "      ]\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "      \"name\": \"node3\",\r\n"
			+ "      \"ip\": \"172.16.10.117\",\r\n"
			+ "      \"nodeTypes\": [\r\n"
			+ "        \"worker\"\r\n"
			+ "      ]\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "      \"name\": \"node4\",\r\n"
			+ "      \"ip\": \"172.16.10.118\",\r\n"
			+ "      \"nodeTypes\": [\r\n"
			+ "        \"worker\"\r\n"
			+ "      ]\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "      \"name\": \"node5\",\r\n"
			+ "      \"ip\": \"172.16.10.119\",\r\n"
			+ "      \"nodeTypes\": [\r\n"
			+ "        \"worker\"\r\n"
			+ "      ]\r\n"
			+ "    }\r\n"
			+ "  ]\r\n"			
			+ "}\r\n"		
			+"```\n"		
)
	@PostMapping("/cluster")
	public String createCluster(@RequestBody Map<String, Object> param) throws IOException {
		Gson gson = new Gson();		
		String json = gson.toJson(param);		
		CreateClusterInfo clusterInfo = gson.fromJson(json, CreateClusterInfo.class);
		clusterService.createCluster(clusterInfo);
		return "success";
	}
	
	
}

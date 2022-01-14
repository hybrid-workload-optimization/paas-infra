package kr.co.sptek.paas.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.sptek.paas.model.CreateClusterInfo;
import kr.co.sptek.paas.model.ProcessResult;
import kr.co.sptek.paas.service.cluster.ClusterService;
import reactor.core.publisher.Flux;

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
			+"***출력부***\n"
			+"```\n"
			+"{\n"
			+"	\"exitCode\": \"작업에 대한 결과 코드 0=성공, 그외=에러\",\n"
			+"	\"message\": \"Error message.\"\n"
			+"}\n"
			+"```\n"
	)
	@PostMapping("/create")
	public @ResponseBody ProcessResult create(@RequestBody Map<String, Object> param) throws IOException {
		Gson gson = new Gson();		
		String json = gson.toJson(param);		
		CreateClusterInfo clusterInfo = gson.fromJson(json, CreateClusterInfo.class);
		ProcessResult result = clusterService.createCluster(clusterInfo);
		return result;
	}	
	
	@ApiOperation(value="Cluster Scale 조정.",
			notes=""
					+"***입력부***\n"
					+"```\n"
					+"{\r\n"
					+ "  \"clusterName\": \"cluster1\",\r\n"
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
					+"***출력부***\n"
					+"```\n"
					+"{\n"
					+"	\"exitCode\": \"작업에 대한 결과 코드 0=성공, 그외=에러\",\n"
					+"	\"message\": \"Error message.\"\n"
					+"}\n"
					+"```\n"
			)
	@PostMapping("/scale")
	public @ResponseBody ProcessResult scale(@RequestBody Map<String, Object> param) throws IOException {
		Gson gson = new Gson();		
		String json = gson.toJson(param);		
		CreateClusterInfo clusterInfo = gson.fromJson(json, CreateClusterInfo.class);
		ProcessResult result = clusterService.updateScale(clusterInfo);
		return result;
	}
	
	@ApiOperation(value="Cluster Scale 조정.",
			notes=""
					+"***입력부***\n"
					+"```\n"
					+"{\r\n"
					+ "  \"clusterName\": \"cluster1\",\r\n"		
					+ "}\r\n"		
					+"```\n"
					+"***출력부***\n"
					+"```\n"
					+"{\n"
					+"	\"exitCode\": \"작업에 대한 결과 코드 0=성공, 그외=에러\",\n"
					+"	\"message\": \"Error message.\"\n"
					+"}\n"
					+"```\n"
			)
	@DeleteMapping("/remove")
	public @ResponseBody ProcessResult delete(@RequestBody Map<String, String> param) throws IOException {
		String clusterName = param.get("clusterName");
		ProcessResult result = clusterService.deleteCluster(clusterName);
		return result;
	}
	
	
	@GetMapping(path = "/log", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> getClusterJobLog(@RequestParam String clusterName) {
		return Flux.create(sink -> {
			clusterService.registryClusterJobLog(clusterName, sink::next);
        });
	}
	
	
	@GetMapping(path = "/cluster2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> createCluster2() throws IOException {
		Gson gson = new Gson();		
		//String json = gson.toJson(param);
		String json = "{\r\n"
				+ "  \"clusterName\": \"cluster1\",\r\n"
				+ "  \"provider\": \"private\",\r\n"
				+ "  \"userName\": \"root\",\r\n"
				+ "  \"nodes\": [\r\n"
				+ "    {\r\n"
				+ "      \"name\": \"node1\",\r\n"
				+ "      \"ip\": \"172.16.10.115\",\r\n"
				+ "      \"nodeTypes\": [\r\n"
				+ "        \"master\"\r\n"
				+ "      ]\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"name\": \"node2\",\r\n"
				+ "      \"ip\": \"172.16.10.116\",\r\n"
				+ "      \"nodeTypes\": [\r\n"
				+ "        \"master\"\r\n"
				+ "      ]\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"name\": \"node3\",\r\n"
				+ "      \"ip\": \"172.16.10.117\",\r\n"
				+ "      \"nodeTypes\": [\r\n"
				+ "        \"master\"\r\n"
				+ "      ]\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"name\": \"node4\",\r\n"
				+ "      \"ip\": \"172.16.10.118\",\r\n"
				+ "      \"nodeTypes\": [\r\n"
				+ "        \"worker\"\r\n"
				+ "      ]\r\n"
				+ "    }\r\n"
				+ "  ]\r\n"
				+ "}";
		CreateClusterInfo clusterInfo = gson.fromJson(json, CreateClusterInfo.class);
		
		
		return Flux.create(sink -> {
			clusterService.createCluster(clusterInfo);
        });
	}
	
	
	
	
	/**
	 * 클러스터 스케일 조정
	 */
	public void scaleCluster() {
		
	}
	
	/**
	 * 클러스터 삭제.
	 */
	public void removeCluster() {
		
	}
}

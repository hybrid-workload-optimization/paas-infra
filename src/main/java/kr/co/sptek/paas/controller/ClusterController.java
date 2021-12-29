package kr.co.sptek.paas.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = {"Cluster"})
@RequestMapping("/cluster")
public class ClusterController {

	@ApiOperation(value="Cluster »ý¼º")
	@PostMapping("/cluster")
	public String createCluster() throws IOException {
		return "success";
	}
}

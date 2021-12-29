package kr.co.sptek.paas.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.sptek.paas.model.Cluster;

@RestController
@Api(tags = {"Cluster"})
@RequestMapping("/cluster")
public class ClusterController {

	@ApiOperation(value="Cluster ����")
	@PostMapping("/cluster")
	public String createCluster(@ModelAttribute Cluster cluster) throws IOException {
		return "success";
	}
}

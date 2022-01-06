package kr.co.sptek.paas.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import kr.co.sptek.paas.service.KubesprayService;

@RestController
@Api(tags = {"Kubespray"})
@RequestMapping("/kubespray")
public class KubesprayController {
	
	@Autowired
	private KubesprayService kubesprayService;

	@GetMapping("version")
	public String[] getVersions() throws IOException {
		return kubesprayService.getKubesprayVersion();
	}
	
	@PostMapping("install")
	public String installKubespray() throws IOException {
		kubesprayService.installKubespray("2.10");
		return "success";
	}
}

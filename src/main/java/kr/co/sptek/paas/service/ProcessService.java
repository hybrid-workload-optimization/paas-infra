package kr.co.sptek.paas.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.sptek.paas.model.ProcessResult;

@Service
public class ProcessService {	
	private static Logger logger = LoggerFactory.getLogger(ProcessService.class);

	public ProcessResult process(String command, String dir, String[] excludeWord) throws IOException {		
		ProcessBuilder builder = new ProcessBuilder();
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		if (isWindows) {
		    builder.command("cmd.exe", "/c", command);
		} else {
		    builder.command("sh", "-c", command);
		}
		
		builder.directory(new File(dir));
		Process process = builder.start();			
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = reader.readLine()) != null) {
			if(excludeWord != null) {
				for(String word : excludeWord) {
					if(line.contains(word)) {
						continue;
					}
				}
			}
			
			sb.append(line);
			sb.append("\n");
		}
		
		int exitCode = -1;
		try {
			exitCode = process.waitFor();
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		
		ProcessResult result = new ProcessResult();
		result.setExitCode(exitCode);
		result.setMessage(sb.toString());		
		return result;
	}
}

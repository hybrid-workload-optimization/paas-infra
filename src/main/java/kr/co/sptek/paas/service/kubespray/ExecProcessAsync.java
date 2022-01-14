package kr.co.sptek.paas.service.kubespray;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExecProcessAsync {	
	private static Logger logger = LoggerFactory.getLogger(ExecProcess.class);
	
	public List<Consumer<String>> listeners;
	public StringBuffer sbLog;
	
	public ExecProcessAsync() {
		this.listeners = new CopyOnWriteArrayList<>();
		this.sbLog = new StringBuffer();
	}

	/**
	 * 커맨드 실행.
	 * @param command
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public int process(String command, String dir) throws IOException {		
		ProcessBuilder builder = new ProcessBuilder();
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		if (isWindows) {
		    builder.command("cmd.exe", "/c", command);
		} else {
		    builder.command("sh", "-c", command);
		}
		
		builder.directory(new File(dir));
		Process process = builder.start();
		
		StreamGobbler streamGobbler = 
				  new StreamGobbler(process.getInputStream());
		
		Executors.newSingleThreadExecutor().submit(streamGobbler);
		
		int exitCode = -1;
		try {
			exitCode = process.waitFor();
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		return exitCode;
	}
	
	/**
	 * 로그를 수신할 리스너 등록.
	 * @param listener
	 */
	public void register(Consumer<String> listener) {
        listeners.add(listener);
        String perLog = sbLog.toString();
        if(perLog != null &&  perLog.length() > 0) {
        	listener.accept(sbLog.toString());
        }
    }
	
	
	class StreamGobbler implements Runnable {
	    private InputStream inputStream;

	    public StreamGobbler(InputStream inputStream) {
	        this.inputStream = inputStream;
	    }

	    @Override
	    public void run() {   	
	        //new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
	    	
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					for(Consumer<String> c : listeners) {
						c.accept(line);
						sbLog.append(String.format("%s\n", line));
						logger.info(line);
					}
				}
			} catch (IOException e) {
				logger.error("", e);
			}
	    }
	}
}

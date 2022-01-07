package kr.co.sptek.paas.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

class StreamGobbler implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;

    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
    	/*
    	BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream));
    	try {
    		while(reader.ready()) {
    			String line = reader.readLine();
    			System.out.println(line);
    		}
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	*/
    	
    	
        new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
    }
}
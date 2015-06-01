package io.gamemachine.core;

import io.gamemachine.config.AppConfig;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;

public class ExternalProcess implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ExternalProcess.class);
	
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static String runnerPath = AppConfig.envRoot+File.separator+"runners";
	private static Map<String,Boolean> processList = new ConcurrentHashMap<String,Boolean>();
	
    public String path;
    
    public static void start(String command) {
    	if (processList.containsKey(command)) {
    		logger.warn("Process "+command+" already started");
    		return;
    	}
    	
    	ExternalProcess manager = new ExternalProcess(command);
    	executor.execute(manager);
    	processList.put(command, true);
    	logger.warn("Process "+command+" started");
    }
    
    public ExternalProcess(String commandName) {
    	path = commandName;
    }
	@Override
	public void run() {
		try {
			int rvalue = new ProcessExecutor().command(path).destroyOnExit().execute().getExitValue();
			logger.warn("Process exit value was "+rvalue);
		} catch (InvalidExitValueException | IOException | InterruptedException | TimeoutException e) {
			e.printStackTrace();
		}
	}
    
    
}

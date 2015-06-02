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

import com.google.common.base.Strings;

public class ExternalProcess implements Runnable {

	public static class ProcessInfo {
		public String startScript;
		public String executable;
	}

	private static final Logger logger = LoggerFactory.getLogger(ExternalProcess.class);

	private static ExecutorService executor = Executors.newCachedThreadPool();
	private static String runnerPath = AppConfig.envRoot + File.separator + "runners";
	private static Map<String, ProcessInfo> processList = new ConcurrentHashMap<String, ProcessInfo>();

	public String path;

	public static void stopAll() {
		Runtime rt = Runtime.getRuntime();

		for (ProcessInfo info : processList.values()) {
			if (Strings.isNullOrEmpty(info.executable)) {
				logger.warn("Invalid executable name "+info.executable);
				continue;
			}
			
			try {
				String command;
				if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
					command = "taskkill /F /IM "+info.executable;
				} else {
					command = "pkill -f \""+info.executable+"\"";
				}
				logger.debug("Killing "+info.executable+" with "+command);
				rt.exec(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		executor.shutdownNow();
	}

	public static void start(ProcessInfo info) {
		String key = info.startScript + info.executable;
		if (processList.containsKey(key)) {
			logger.warn("Process " + info.executable + " already started");
			return;
		}

		ExternalProcess manager = new ExternalProcess(info.startScript);
		executor.execute(manager);
		processList.put(key, info);
		logger.warn("Process " + info.executable + " started at " + info.startScript);
	}

	public ExternalProcess(String commandName) {
		path = commandName;
	}

	@Override
	public void run() {
		try {
			ProcessExecutor pex = new ProcessExecutor();
			int rvalue = pex.command(path).execute().getExitValue();
			logger.warn("Process exit value was " + rvalue);
		} catch (InvalidExitValueException | IOException | InterruptedException | TimeoutException e) {
			e.printStackTrace();
		}
	}

}

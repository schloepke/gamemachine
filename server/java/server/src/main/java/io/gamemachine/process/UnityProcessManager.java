package io.gamemachine.process;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import io.gamemachine.config.AppConfig;

public class UnityProcessManager {
	private static final Logger logger = LoggerFactory.getLogger(UnityProcessManager.class);
	private static ExecutorService executor = Executors.newCachedThreadPool();
	private static Map<String, ExternalProcess> processList = new ConcurrentHashMap<String, ExternalProcess>();
	
	private boolean isCommandLine = false;
	
	public UnityProcessManager(boolean isCommandLine) {
		this.isCommandLine = isCommandLine;
	}
	
	public String getUnityPath(int instance) {
		List<String> instancePath;
		
		if (isCommandLine) {
			String cwd = Paths.get(".").toAbsolutePath().normalize().toString();
			instancePath = new ArrayList<String>(Arrays.asList(cwd,"unity","instance"+instance));
		} else {
			instancePath = new ArrayList<String>(Arrays.asList(AppConfig.envRoot,"process_manager","unity","instance"+instance));
		}
		
		if (ExternalProcess.os() == ExternalProcess.OS.WIN) {
			instancePath.add("start.bat");
		} else {
			instancePath.add("start.sh");
		}
		return Joiner.on(File.separator).join(instancePath);
	}
	
	public String getUnityExe(int instance) {
		if (ExternalProcess.os() == ExternalProcess.OS.WIN) {
			return "unityServer"+instance+".exe";
		} else {
			return "unityServer"+instance;
		}
	}
	
	public Config getConfig() {
		if (isCommandLine) {
			String cwd = Paths.get(".").toAbsolutePath().normalize().toString();
			String configPath = cwd+File.separator+"process_manager.conf";
			Path path = Paths.get(configPath);
			
			try {
				if (new File(configPath).exists()) {
					String data = new String(Files.readAllBytes(path));
					return ConfigFactory.parseString(data);
				} else {
					logger.warn("Config file not found at "+configPath);
					return null;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return AppConfig.getConfig();
		}
	}
	
	public boolean exists(String name) {
		if (processList.containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}
	
	public ExternalProcess getProcessInfo(String name) {
		if (processList.containsKey(name)) {
			return processList.get(name);
		} else {
			return null;
		}
	}
	
	public static void stopAll() {
		for (ExternalProcess info : processList.values()) {
			info.stop();
		}
		executor.shutdownNow();
	}

	public void startAll() {
		Config config = getConfig();
		if (config == null) {
			logger.warn("Unable to load config");
			return;
		}
		
		int instanceCount = config.getInt("gamemachine.unity_processes");
		for (int i = 0; i<instanceCount; i++) {
			String name = UUID.randomUUID().toString();
			String exePath = getUnityPath(i);
			String exeName = getUnityExe(i);
			logger.warn("Adding instance "+exePath+" "+exeName);
			ExternalProcess info = new ExternalProcess(exeName,exePath,exeName);
			add(info);
		}
	}
		
	public void add(ExternalProcess info) {
		String key = info.name;
		if (exists(key)) {
			logger.info("Process " + info.executable + " exists, not adding");
			return;
		}
		
		processList.put(key, info);
	}
	
	private void stop(ExternalProcess info) {
		info.stop();
		if (processList.containsKey(info.name())) {
			processList.remove(info.name());
		}
	}
	
	private void start(ExternalProcess info) {
		executor.execute(info);
		processList.put(info.name(), info);
	}
	
	public void checkStatus() {
		for (ExternalProcess process : processList.values()) {
			if (process.isRunning()) {
				if (process.status == ExternalProcess.Status.DOWN) {
					logger.info("Process "+process.name+" is UP");
				}
				process.status = ExternalProcess.Status.UP;
			} else {
				if (process.status == ExternalProcess.Status.UP) {
					logger.info("Process "+process.name+" is DOWN");
				}
				process.status = ExternalProcess.Status.DOWN;
				start(process);
			}
			
		}
	}
	
}

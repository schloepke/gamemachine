package io.gamemachine.process;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import io.gamemachine.config.AppConfig;
import io.gamemachine.messages.ProcessCommand;

public class ProcessManager {
	private static final Logger logger = LoggerFactory.getLogger(ProcessManager.class);
	private static ExecutorService executor = Executors.newCachedThreadPool();
	private static Map<String, ExternalProcess> processList = new ConcurrentHashMap<String, ExternalProcess>();
	
	private boolean isCommandLine = false;
	
	public ProcessManager(boolean isCommandLine) {
		this.isCommandLine = isCommandLine;
	}
	
	public Config getConfig() {
		if (isCommandLine) {
			String cwd = Paths.get(".").toAbsolutePath().normalize().toString();
			String configPath = cwd+"/process_manager.conf";
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
		
		List<? extends Config> values = config.getConfigList("gamemachine.processes");
		for (Config value : values) {
			
			ExternalProcess info = new ExternalProcess(value.getString("start_script"),value.getString("executable"));
			boolean enabled = value.getBoolean("enabled");
			
			if (enabled) {
				add(info);
			} else {
				logger.debug(info.executable+" not enabled, skipping");
			}
		}
	}
	
	public void DoCommand(ProcessCommand command) {
		ExternalProcess process = new ExternalProcess(command.startScript,command.executable);
		if (command.action == ProcessCommand.Action.Start) {
			start(process);
		} else if (command.action == ProcessCommand.Action.Stop) {
			stop(process);
		} else if (command.action == ProcessCommand.Action.Restart) {
			stop(process);
			start(process);
		}
	}
	
	public void add(ExternalProcess info) {
		String key = info.name();
		if (exists(key)) {
			logger.warn("Process " + info.executable + " exists, not adding");
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
				process.status = ExternalProcess.Status.UP;
			} else {
				process.status = ExternalProcess.Status.DOWN;
				start(process);
			}
			logger.warn("Process "+process.executable+" is "+process.status.toString());
		}
	}
	
}

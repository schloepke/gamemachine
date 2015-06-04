package io.gamemachine.process;

import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessManager extends GameMessageActor {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessManager.class);
	public static String name = ProcessManager.class.getSimpleName();
	private static ExecutorService executor = Executors.newCachedThreadPool();
	private static Map<String, ExternalProcess> processList = new ConcurrentHashMap<String, ExternalProcess>();


	private long checkInterval = 10000l;
	
	
	public static boolean exists(String name) {
		if (processList.containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static ExternalProcess getProcessInfo(String name) {
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

	
	public static void add(ExternalProcess info) {
		String key = info.name();
		if (exists(key)) {
			logger.warn("Process " + info.executable + " exists, not adding");
			return;
		}
		
		processList.put(key, info);
	}
	
	private void start(ExternalProcess info) {
		executor.execute(info);
		processList.put(info.name(), info);
	}

	@Override
	public void awake() {
		scheduleOnce(checkInterval,"check");
	}

	public void onTick(String message) {
		checkStatus();
		scheduleOnce(checkInterval,"check");
	}
	
	private void checkStatus() {
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
	@Override
	public void onGameMessage(GameMessage gameMessage) {
		// TODO Auto-generated method stub
		
	}
	

}

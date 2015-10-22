package io.gamemachine.core;

import io.gamemachine.config.AppConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public abstract class Plugin {

	private static final Logger logger = LoggerFactory.getLogger(Plugin.class);
	private static Map<String,Config> configs = new ConcurrentHashMap<String,Config>();
	
	private static String pluginPath = AppConfig.envRoot+File.separator+"java"+File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator+"plugins";
	
	public abstract void start();
	
	public static String getJsonConfig(Class klass) {
		String key = klass.getSimpleName();
			String configPath = pluginPath+File.separator+key+".json";
			Path path = Paths.get(configPath);
			try {
				return new String(Files.readAllBytes(path));
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
	}
	
	public static Config getConfig(Class klass) {
		String key = klass.getSimpleName();
		if (!configs.containsKey(key)) {
			String configPath = pluginPath+File.separator+key+".conf";
			File file = new File(configPath);
			if (file.isFile()) {
				configs.put(key, ConfigFactory.parseFile(file));
			} else {
				logger.warn("Plugin config not found: "+configPath);
			}
		}
		return configs.get(key);
	}
	
}

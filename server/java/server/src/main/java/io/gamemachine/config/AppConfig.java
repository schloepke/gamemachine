package io.gamemachine.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class AppConfig {
	
	private static boolean orm;
	private static String env;
	private static String configName;
	public static String envRoot;
	private static String defaultGameId;
	private static int worldOffset;
	private static long gridExpiration;
	private static List<String> plugins = new ArrayList<String>();
	private static int akkaPort;
	private static String akkaHost;
	private static String agentSecret;
	
	public static Config getConfig() {
		String path = AppConfig.envRoot+File.separator+"config"+File.separator+configName+".conf";
		File file = new File(path);
		return ConfigFactory.parseFile(file);
	}
	
	public static void setConfigName(String name) {
		AppConfig.configName = name;
	}
	
	public static String getConfigName() {
		return AppConfig.configName;
	}
	
	public static void setGridExpiration(long gridExpiration) {
		AppConfig.gridExpiration = gridExpiration;
	}
	
	public static long getGridExpiration() {
		return AppConfig.gridExpiration;
	}
	
	public static void setWorldOffset(int worldOffset) {
		AppConfig.worldOffset = worldOffset;
	}
	
	public static int getWorldOffset() {
		return AppConfig.worldOffset;
	}
	
	public static List<String> getPlugins() {
		return plugins;
	}
	
	public static void setPlugins(List<String> plugins) {
		AppConfig.plugins = plugins;
	}
	
	public static boolean getOrm() {
		return orm;
	}
	
	public static void setOrm(boolean orm) {
		AppConfig.orm = orm;
	}
	
	public static String getAgentSecret() {
		return agentSecret;
	}

	public static void setAgentSecret(String agentSecret) {
		AppConfig.agentSecret = agentSecret;
	}
	
	public static String getEnv() {
		return env;
	}

	public static void setEnv(String env) {
		AppConfig.env = env;
	}

	public static String getEnvRoot() {
		return envRoot;
	}

	public static void setEnvRoot(String envRoot) {
		AppConfig.envRoot = envRoot;
	}
	
	public static String getDefaultGameId() {
		return defaultGameId;
	}

	public static void setDefaultGameId(String defaultGameId) {
		AppConfig.defaultGameId = defaultGameId;
	}

	public static String getAkkaHost() {
		return akkaHost;
	}
	
	public static void setAkkaHost(String host) {
		akkaHost = host;
	}
	
	public static int getAkkaPort() {
		return akkaPort;
	}
	
	public static void setAkkaPort(int port) {
		akkaPort = port;
	}
	
	
	public static class Client {
		private static int idleTimeout;
		private static String protocol;
		private static String tcpHost;
		private static int tcpPort;
		private static String udpHost;
		private static int udpPort;
		
		public static String getUdpHost() {
			return udpHost;
		}
		
		public static void setUdpHost(String udpHost) {
			Client.udpHost = udpHost;
		}
		
		public static int getUdpPort() {
			return udpPort;
		}
		
		public static void setUdpPort(int udpPort) {
			Client.udpPort = udpPort;
		}
		
		public static String getTcpHost() {
			return tcpHost;
		}
		
		public static void setTcpHost(String tcpHost) {
			Client.tcpHost = tcpHost;
		}
		
		public static int getTcpPort() {
			return tcpPort;
		}
		
		public static void setTcpPort(int tcpPort) {
			Client.tcpPort = tcpPort;
		}
		
		
		public static String getProtocol() {
			return protocol;
		}
		
		public static void setProtocol(String protocol) {
			Client.protocol = protocol;
		}
		
		public static int getIdleTimeout() {
			return idleTimeout;
		}

		public static void setIdleTimeout(int idleTimeout) {
			Client.idleTimeout = idleTimeout;
		}

	}
	
    public static class Datastore {
    	private static String store;
    	private static String serialization;
    	private static long cacheWriteInterval;
    	private static long cacheWritesPerSecond;
    	private static String mapdbPath;
    	
		public static String getStore() {
			return store;
		}
		public static void setStore(String store) {
			Datastore.store = store;
		}
		public static String getSerialization() {
			return serialization;
		}
		public static void setSerialization(String serialization) {
			Datastore.serialization = serialization;
		}
		public static long getCacheWriteInterval() {
			return cacheWriteInterval;
		}
		public static void setCacheWriteInterval(long cacheWriteInterval) {
			Datastore.cacheWriteInterval = cacheWriteInterval;
		}
		public static long getCacheWritesPerSecond() {
			return cacheWritesPerSecond;
		}
		public static void setCacheWritesPerSecond(long cacheWritesPerSecond) {
			Datastore.cacheWritesPerSecond = cacheWritesPerSecond;
		}
		public static String getMapdbPath() {
			return mapdbPath;
		}
		public static void setMapdbPath(String mapdbPath) {
			Datastore.mapdbPath = mapdbPath;
		}
		
    }
    
	public static class Couchbase {
		private static String bucket;
		private static String password;
		private static List<String> servers;
		public static String getBucket() {
			return bucket;
		}
		public static void setBucket(String bucket) {
			Couchbase.bucket = bucket;
		}
		public static String getPassword() {
			return password;
		}
		public static void setPassword(String password) {
			Couchbase.password = password;
		}
		public static List<String> getServers() {
			return servers;
		}
		public static void setServers(List<String> servers) {
			Couchbase.servers = servers;
		}
	}
	
	
	public static class Jdbc {
		private static String hostname;
		private static int port;
		private static String database;
		private static String ds;
		private static String driver;
		private static String username;
		private static String password;
		private static String url;
		
		public static String getUrl() {
			return url;
		}
		
		public static void SetUrl(String url) {
			Jdbc.url = url;
		}
		
		public static String getHostname() {
			return hostname;
		}
		public static void setHostname(String hostname) {
			Jdbc.hostname = hostname;
		}
		public static int getPort() {
			return port;
		}
		public static void setPort(int port) {
			Jdbc.port = port;
		}
		public static String getDatabase() {
			return database;
		}
		public static void setDatabase(String database) {
			Jdbc.database = database;
		}
		public static String getDs() {
			return ds;
		}
		public static void setDs(String ds) {
			Jdbc.ds = ds;
		}
		public static String getDriver() {
			return driver;
		}
		public static void setDriver(String driver) {
			Jdbc.driver = driver;
		}
		public static String getUsername() {
			return username;
		}
		public static void setUsername(String username) {
			Jdbc.username = username;
		}
		public static String getPassword() {
			return password;
		}
		public static void setPassword(String password) {
			Jdbc.password = password;
		}
	}
	
	public static class Gamecloud {
		private static String host;
		private static String user;
		private static String apiKey;
		
		public static String getHost() {
			return host;
		}
		public static void setHost(String host) {
			Gamecloud.host = host;
		}
		public static String getUser() {
			return user;
		}
		public static void setUser(String user) {
			Gamecloud.user = user;
		}
		public static String getApiKey() {
			return apiKey;
		}
		public static void setApiKey(String apiKey) {
			Gamecloud.apiKey = apiKey;
		}
	}
	
	public static class Handlers {
		
		private static String auth;
		private static String team;
		
		
		public static String getAuth() {
			return auth;
		}
		public static void setAuth(String auth) {
			Handlers.auth = auth;
		}
		public static String getTeam() {
			return team;
		}
		public static void setTeam(String team) {
			Handlers.team = team;
		}
	}

	
}

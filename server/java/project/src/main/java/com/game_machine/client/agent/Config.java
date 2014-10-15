package com.game_machine.client.agent;

import com.typesafe.config.ConfigFactory;

public class Config {

	private com.typesafe.config.Config conf;
	private String gameId;
	private String cloudHost;
	private String cloudUser;
	private String cloudApiKey;
	private String defaultHost;
	private int defaultPort;
	
	private static class LazyHolder {
		private static final Config INSTANCE = new Config();
	}

	public static Config getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private Config() {
		conf = ConfigFactory.load();
		gameId = conf.getString("game.id");
		cloudHost = conf.getString("gamecloud.host");
		cloudUser = conf.getString("gamecloud.user");
		cloudApiKey = conf.getString("gamecloud.api_key");
		defaultHost = conf.getString("game.host");
		defaultPort = conf.getInt("game.port");
	}

	public String getGameId() {
		return gameId;
	}

	public String getCloudUser() {
		return cloudUser;
	}

	public String getCloudHost() {
		return cloudHost;
	}

	public String getCloudApiKey() {
		return cloudApiKey;
	}

	public String getDefaultHost() {
		return defaultHost;
	}

	public int getDefaultPort() {
		return defaultPort;
	}

}

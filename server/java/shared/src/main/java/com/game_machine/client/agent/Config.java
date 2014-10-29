package com.game_machine.client.agent;

import java.util.Map;

import com.typesafe.config.ConfigFactory;

public class Config {

	private com.typesafe.config.Config conf;
	private String gameId;
	private String cloudHost;
	private String cloudUser;
	private String cloudApiKey;
	private String defaultHost;
	private int defaultPort;
	private String authtoken;
	private String playerId;
	private int rateLimit;

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
		authtoken = conf.getString("game.authtoken");
		playerId = conf.getString("game.player_id");
		setRateLimit(conf.getInt("game.rate_limit"));
		setFromEnv();
	}

	public void setFromEnv() {
		Map<String, String> env = System.getenv();
		if (env.containsKey("GAME_ID")) {
			gameId = env.get("GAME_ID");
		}
		if (env.containsKey("PLAYER_ID")) {
			playerId = env.get("PLAYER_ID");
		}
		if (env.containsKey("AUTHTOKEN")) {
			authtoken = env.get("AUTHTOKEN");
		}
		if (env.containsKey("USER")) {
			cloudUser = env.get("USER");
		}
		if (env.containsKey("API_KEY")) {
			cloudApiKey = env.get("API_KEY");
		}
		if (env.containsKey("CLOUD_HOST")) {
			cloudHost = env.get("CLOUD_HOST");
		}
		if (env.containsKey("RATE_LIMIT")) {
			rateLimit = Integer.parseInt(env.get("RATE_LIMIT"));
		}
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

	public String getAuthtoken() {
		return authtoken;
	}

	public String getPlayerId() {
		return playerId;
	}

	public int getRateLimit() {
		return rateLimit;
	}

	public void setRateLimit(int rateLimit) {
		this.rateLimit = rateLimit;
	}

}

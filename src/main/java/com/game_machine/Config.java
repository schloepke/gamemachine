package com.game_machine;

import com.typesafe.config.ConfigFactory;

public class Config {

	public static com.typesafe.config.Config config;

	public static String logLevel;
	public static String memcacheHost;
	public static String memcachePort;

	public static Boolean udpEnabled;
	public static int udpPort;
	public static String udpHost;
	public static String udpEncoding;

	public static Boolean udtEnabled;
	public static int udtPort;
	public static String udtHost;
	public static String udtEncoding;
	
	public static String objectStore;
	public static String gameHandler;
	public static String gameHandlerRouter;


	static {
		config = ConfigFactory.load();

		logLevel = config.getString("game_machine.loglevel");

		memcacheHost = config.getString("game_machine.memcached.host");
		memcachePort = config.getString("game_machine.memcached.port");

		udpEnabled = config.getBoolean("game_machine.udp.enabled");
		udpPort = config.getInt("game_machine.udp.port");
		udpHost = config.getString("game_machine.udp.host");
		udpEncoding = config.getString("game_machine.udp.encoding");

		udtEnabled = config.getBoolean("game_machine.udt.enabled");
		udtPort = config.getInt("game_machine.udt.port");
		udtHost = config.getString("game_machine.udt.host");
		udtEncoding = config.getString("game_machine.udt.encoding");
		
		objectStore = config.getString("game_machine.objectdb.store");
		gameHandler = config.getString("game_machine.gameHandler");
		gameHandlerRouter = config.getString("game_machine.gameHandlerRouter");
	}
	
}

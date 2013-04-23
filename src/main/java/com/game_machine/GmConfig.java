package com.game_machine;

import java.util.logging.Logger;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class GmConfig {

	public static Config config;

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

	public static void load() {

		config = ConfigFactory.load();
		
		logLevel = config.getString("server.loglevel");
		
		memcacheHost = config.getString("server.memcached.host");
		memcachePort = config.getString("server.memcached.port");
		
		udpEnabled = config.getBoolean("server.udp.enabled");
		udpPort = config.getInt("server.udp.port");
		udpHost = config.getString("server.udp.host");
		udpEncoding = config.getString("server.udp.encoding");
		
		udtEnabled = config.getBoolean("server.udt.enabled");
		udtPort = config.getInt("server.udt.port");
		udtHost = config.getString("server.udt.host");
		udtEncoding = config.getString("server.udt.encoding");
	}
}

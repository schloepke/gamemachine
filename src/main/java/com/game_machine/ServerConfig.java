package com.game_machine;

import java.util.logging.Logger;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ServerConfig {

	private static final Logger log = Logger.getLogger(ServerConfig.class.getName());
	
	public static final Config config = ConfigFactory.load("server.conf");
	
	
	public static final int udpPort = config.getInt("server.udp.port");
	public static final String udpHost = config.getString("server.udp.host");
	public static final String udpEncoding = config.getString("server.udp.encoding");
	
	public static final int udtPort = config.getInt("server.udt.port");
	public static final String udtHost = config.getString("server.udt.host");
	public static final String udtEncoding = config.getString("server.udt.encoding");
	
	
}

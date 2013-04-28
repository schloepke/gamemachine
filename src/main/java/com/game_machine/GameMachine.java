package com.game_machine;

import java.util.logging.Logger;

import com.game_machine.core.actors.Root;

public class GameMachine {

	private static final Logger log = Logger.getLogger(GameMachine.class.getName());

	public static void main(String [] args) {
		new GameMachine().startup();
	}
	
	public static void test() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { 
		    	System.exit(0);
		    }
		 });
	}
	
	public void startup() {
		GmConfig.load();
		Root.start();
		
	}

	public void shutdown() {
		Root.shutdown();
		GmContext.getUdpServer().shutdown();
	}

}

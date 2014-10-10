package com.game_machine.codeblocks.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	private static final Logger logger = LoggerFactory.getLogger(Test.class);
	
	public static void sendMessage(String message) {
		logger.warn("sendMessage called");
	}
}

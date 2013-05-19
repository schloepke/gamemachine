package com.game_machine.test;

import java.util.logging.Logger;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.game_machine.GameMachine;
import com.game_machine.net.client.Client;


public class ServerTest {

	public static final Logger log = Logger.getLogger(ServerTest.class.getName());

	@BeforeSuite
	public void setup() {
		GameMachine.start();
	}

	@AfterSuite
	public void teardown() {
		GameMachine.stop();
	}

		
	@Test
	public void clientTest() {
		try {
			Client.test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

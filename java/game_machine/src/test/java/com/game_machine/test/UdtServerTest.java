package com.game_machine.test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.game_machine.udt_server.Client;
import com.game_machine.udt_server.Server;

public class UdtServerTest {

	
	@Test
	public void runServer()  throws Exception {
		Thread t = Server.start(1234);
		Thread.sleep(2);
		Client.start();
		Thread.sleep(5);
		t.stop();
		t.join();
		
		
	}
}

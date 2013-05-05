package com.game_machine.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.game_machine.Config;
import com.game_machine.GameMachine;
import com.game_machine.NetMessage;
import com.game_machine.ProtobufMessages.ClientMessage;
import com.game_machine.net.client.ClientCallable;
import com.game_machine.net.client.MessageBuilder;
import com.game_machine.net.client.UdpClient;
import com.game_machine.net.client.UdtClient;
import com.game_machine.net.server.UdtServer;

public class ServerTest {

	public static final Logger log = Logger.getLogger(ServerTest.class.getName());

	@BeforeClass
	public void setup() {
		GameMachine.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void teardown() {
		GameMachine.stop();
	}

	// @Test
	public void runServer() throws Exception {
		UdtServer.logLevel = Level.parse(Config.logLevel);
		UdtClient.logLevel = Level.INFO;
	}

	
	//@Test
	public void UdpEcho() {
		final UdpClient client = new UdpClient(Config.udpHost, Config.udpPort);

		client.setCallback(new ClientCallable() {
			public void apply(byte[] bytes) {
				String message = new String(bytes);
				if (message.equals("READY")) {
					client.send("STOP".getBytes());
				}
				if (message.equals("STOP")) {
					client.stop();
				}
			}
		});
		client.start();

	}

	@Test
	public void udtEcho() {
		final UdtClient client = new UdtClient(NetMessage.ENCODING_PROTOBUF,Config.udtHost, Config.udtPort);

		client.setCallback(new ClientCallable() {
			public void apply(byte[] bytes) {
				String message = new String(bytes);
				if (message.equals("READY")) {
					client.send("STOP".getBytes());
				}
				if (message.equals("STOP")) {
					client.stop();
				}
			}
		});
		client.start();
	}
	
	//@Test
	public void udtMulti() {
		final UdtClient client = new UdtClient(NetMessage.ENCODING_PROTOBUF,Config.udtHost, Config.udtPort);

		client.setCallback(new ClientCallable() {
			public void apply(byte[] bytes) {
				ClientMessage message = MessageBuilder.decode(bytes);
				String body = message.getBody().toStringUtf8();
				if (body.equals("READY")) {
					for (int i=0;i<100;i++) {
						client.send("TEST MESSAGE".getBytes());
					}
					client.send("STOP".getBytes());
				}
				if (body.equals("STOP")) {
					client.stop();
				}
			}
		});
		client.start();
	}

}

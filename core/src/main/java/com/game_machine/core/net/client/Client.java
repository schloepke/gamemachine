package com.game_machine.core.net.client;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.udt.SocketUDT;
import com.barchart.udt.TypeUDT;
import com.game_machine.core.MessageUtil;
import com.game_machine.entity_system.generated.Components;


public class Client {

	private static final Logger log = LoggerFactory.getLogger(Client.class);

	public static void test() throws Exception {
		SocketUDT socket = new SocketUDT(TypeUDT.DATAGRAM);
		socket.setBlocking(true);
		
		InetSocketAddress address = new InetSocketAddress("192.168.1.2", 8101);
		socket.connect(address);
		

		try {

			for (int i = 0; i < 1; i++) {
				byte[] message = Components.fromEntities(MessageUtil.createEchoCommand()).toByteArray();
				log.info("Client Sent "+ message.length + " bytes");
				socket.send(message);
				final byte[] array = new byte[1024];
				socket.receive(array);
			}
			socket.close();
			 

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}

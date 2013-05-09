package com.game_machine.net.client;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.udt.SocketUDT;
import com.barchart.udt.TypeUDT;
import com.game_machine.MessageUtil;
import com.game_machine.Pb.ClientMessage;


public class Client {

	private static final Logger log = LoggerFactory.getLogger(UdtClientHandler.class);

	public static void test() throws Exception {
		SocketUDT socket = new SocketUDT(TypeUDT.DATAGRAM);
		socket.setBlocking(true);
		
		InetSocketAddress address = new InetSocketAddress("192.168.1.3", 8101);
		socket.connect(address);
		

		try {

			for (int i = 0; i < 100; i++) {
				ClientMessage message = MessageUtil.buildClientMsg("TEST", "localhost");
				socket.send(message.toByteArray());
			}
			socket.close();
			// final int count = is.read(data);
			// final String str = new String(data, 0, count);
			// log.info("|{}|", str);

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}

package com.game_machine.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GameMachine.Messages.ClientMessage;

public class UdpClient {

	private static final Logger log = LoggerFactory.getLogger(UdpClient.class);
	private int port;
	private InetAddress hostAddress;
	private DatagramSocket s;
	private DatagramPacket dp;
	private int timeout;

	public UdpClient(int port, int timeout) {
		this.port = port;
		this.timeout = timeout;

		hostAddress = InetAddress.getLoopbackAddress();
		byte[] in = new byte[2048];
		dp = new DatagramPacket(in, in.length);
	}

	public Boolean connect() {

		try {
			s = new DatagramSocket();
			s.setSoTimeout(timeout);
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Boolean connect(String host, int port) {
		try {
			hostAddress = InetAddress.getByName(host);
			this.port = port;
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return false;
		}

		try {
			s = new DatagramSocket();
			s.setSoTimeout(timeout);
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void send(byte[] bytes) throws IOException {
		DatagramPacket out = new DatagramPacket(bytes, bytes.length, hostAddress, port);
		s.send(out);
	}

	public byte[] receive() throws IOException {
		byte[] in = new byte[4096];
		DatagramPacket inp = new DatagramPacket(in, in.length, hostAddress, port);
		try {
			s.receive(inp);
			byte[] received = Arrays.copyOfRange(inp.getData(), inp.getOffset(), inp.getOffset() + inp.getLength());
			return received;
		} catch (SocketTimeoutException e) {
			System.out.println("Timeout exception waiting for data");
			return null;
		}
	}

	public byte[] sendReceive(byte[] bytes) {
		try {

			int retryCount = 0;
			int retryLimit = 3;

			DatagramPacket out = new DatagramPacket(bytes, bytes.length, hostAddress, port);

			while (true) {
				s.send(out);

				try {
					s.receive(dp);
					byte[] received = Arrays.copyOfRange(dp.getData(), dp.getOffset(), dp.getOffset() + dp.getLength());
					return received;
				} catch (SocketTimeoutException e) {
					try {
						Thread.sleep(retryCount + 1, 0);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if (retryCount > retryLimit) {
						log.warn("Timeout retry limit exceeded " + e + " " + retryCount);
						return null;
					}
					retryCount++;
				}
			}

		} catch (SocketException e1) {
			log.warn("Socket closed " + e1);
			return null;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}

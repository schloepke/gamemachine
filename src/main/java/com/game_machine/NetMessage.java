package com.game_machine;

public class NetMessage {

	// Protocols
	public static final int UDP = 0;
	public static final int UDT = 1;
	public static final int TCP = 2;
	
	// Encoding
	public static final int ENCODING_NONE = 10;
	public static final int ENCODING_PROTOBUF = 11;
	
	public final byte[] bytes;
	public final String host;
	public final int port;
	public final int protocol;
	public final int encoding;
	
	public NetMessage(int protocol, int encoding, byte[] bytes, String host, int port) {
		this.bytes = bytes;
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.encoding = encoding;
	}
	
	public static NetMessage copy(NetMessage message, byte[] bytes) {
		return new NetMessage(message.protocol, message.encoding, bytes, message.host, message.port);
	}
	
	
}

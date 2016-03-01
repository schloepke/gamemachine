package io.gamemachine.core;

import io.gamemachine.messages.ClientMessage;

public class NetMessage {

    // Protocols
    public static final int NETTY_UDP = 0;
    public static final int SIMPLE_UDP = 1;
    public static final int TCP = 2;

    public byte[] bytes;
    public ClientMessage clientMessage;
    public final int protocol;
    public long clientId;
    public int ip;

    public NetMessage(int protocol, int ip, long clientId) {
        this.protocol = protocol;
        this.ip = ip;
        this.clientId = clientId;
    }

}

package io.gamemachine.net.tcp;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.MathHelper;
import io.gamemachine.core.NetMessage;
import io.gamemachine.messages.ClientMessage;
import io.gamemachine.messages.UnityGameMessage;
import io.gamemachine.net.udp.NettyUdpServerHandler.ClientAddress;
import io.gamemachine.routing.Incoming;
import io.gamemachine.unity.UnityMessageHandler;
import io.gamemachine.unity.UnitySync;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;

public class TcpServerHandler extends SimpleChannelInboundHandler<ClientMessage> {

    public class ClientAddress {
        public ChannelHandlerContext ctx;

        public ClientAddress(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
    }

    private static ConcurrentHashMap<Long, ClientAddress> clients = new ConcurrentHashMap<Long, ClientAddress>();
    private static final Logger logger = LoggerFactory.getLogger(TcpServerHandler.class);
    private ActorSelection inbound;
    private ActorSelection rpc;

    public TcpServerHandler() {
        this.inbound = ActorUtil.getSelectionByName(Incoming.name);
        this.rpc = ActorUtil.getSelectionByName(UnityMessageHandler.name);
    }

    public static void removeClient(long clientId) {
        if (clients.containsKey(clientId)) {
            clients.remove(clientId);
        }
    }

    public static void sendMessage(long clientId, ClientMessage clientMessage) {
        ClientAddress clientAddress = clients.get(clientId);
        if (clientAddress == null) {
            logger.warn("ClientAddress not found for " + clientId);
            return;
        }
        clientAddress.ctx.write(clientMessage);
        clientAddress.ctx.flush();
    }

    public static void sendClientMessage(ClientMessage clientMessage, ChannelHandlerContext ctx) {
        ctx.write(clientMessage);
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ClientMessage clientMessage) throws Exception {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        int ip = ByteBuffer.wrap(address.getAddress().getAddress()).getInt();
        long clientId = MathHelper.cantorize(ip, address.getPort());

        if (!clients.containsKey(clientId)) {
            ClientAddress clientAddress = new ClientAddress(ctx);
            clients.put(clientId, clientAddress);
        }

        if (clientMessage.unityMessage != null) {
            rpc.tell(clientMessage.unityMessage, null);
            return;
        }

        NetMessage netMessage = new NetMessage(NetMessage.TCP, ip, clientId);
        netMessage.clientMessage = clientMessage;
        this.inbound.tell(netMessage, null);
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

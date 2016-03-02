package io.gamemachine.net.udp;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.MathHelper;
import io.gamemachine.core.NetMessage;
import io.gamemachine.messages.ClientMessage;
import io.gamemachine.routing.Incoming;
import io.gamemachine.unity.UnityMessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;

@Sharable
public final class NettyUdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    public class ClientAddress {
        public ChannelHandlerContext ctx;
        public InetSocketAddress address;

        public ClientAddress(InetSocketAddress address, ChannelHandlerContext ctx) {
            this.address = address;
            this.ctx = ctx;
        }
    }

    private static ConcurrentHashMap<Long, ClientAddress> clients = new ConcurrentHashMap<Long, ClientAddress>();
    private static final Logger logger = LoggerFactory.getLogger(NettyUdpServerHandler.class);
    public ChannelHandlerContext ctx = null;
    private ActorSelection inbound;
    private ActorSelection rpc;

    public NettyUdpServerHandler() {
        this.inbound = ActorUtil.getSelectionByName(Incoming.name);
        this.rpc = ActorUtil.getSelectionByName(UnityMessageHandler.name);
    }

    // @Override
    // public void exceptionCaught(final ChannelHandlerContext ctx, final
    // Throwable cause) {
    // logger.info("Channel exception caught");
    //
    // try {
    // ctx.close();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    public static void removeClient(long clientId) {
        if (clients.containsKey(clientId)) {
            clients.remove(clientId);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        logger.debug("UDP channel active");
        this.ctx = ctx;
    }

    public static void sendMessage(long clientId, byte[] bytes) {
        ClientAddress clientAddress = clients.get(clientId);
        if (clientAddress == null) {
            logger.warn("ClientAddress not found for " + clientId);
            return;
        }
        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        DatagramPacket packet = new DatagramPacket(buf, clientAddress.address);
        clientAddress.ctx.writeAndFlush(packet);
    }

    public void send(InetSocketAddress address, byte[] bytes, ChannelHandlerContext ctx) {

        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        DatagramPacket packet = new DatagramPacket(buf, address);
        ctx.writeAndFlush(packet);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket m) throws Exception {
        byte[] bytes = new byte[m.content().readableBytes()];
        m.content().readBytes(bytes);

        ClientMessage clientMessage = ClientMessage.parseFrom(bytes);
        int ip = ByteBuffer.wrap(m.sender().getAddress().getAddress()).getInt();
        long clientId = MathHelper.cantorize(ip, m.sender().getPort());

        if (!clients.containsKey(clientId)) {
            ClientAddress clientAddress = new ClientAddress(m.sender(), ctx);
            clients.put(clientId, clientAddress);
        }

        if (clientMessage.unityMessage != null) {
            rpc.tell(clientMessage.unityMessage, null);
            return;
        }

        NetMessage netMessage = new NetMessage(NetMessage.NETTY_UDP, ip, clientId);
        netMessage.clientMessage = clientMessage;

        //logger.debug("MessageReceived length" + bytes.length + " " + new String(bytes));
        this.inbound.tell(netMessage, null);

    }

}

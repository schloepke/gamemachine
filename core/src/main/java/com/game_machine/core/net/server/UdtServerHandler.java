package com.game_machine.core.net.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;
import io.netty.channel.udt.UdtMessage;
import io.netty.channel.udt.nio.NioUdtProvider;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.NetMessage;
import com.game_machine.entity_system.generated.ClientDisconnect;

@Sharable
public class UdtServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(UdtServerHandler.class);
	private UdtServer server;
	private ActorSelection inbound;

	public UdtServerHandler() {
		this.inbound = ActorUtil.getSelectionByName("GameMachine::Endpoints::Udt");
	}

	public void setServer(UdtServer server) {
		this.server = server;
	}

	public void sendToGateway(ChannelHandlerContext ctx, byte[] bytes, int type) {
		log.debug("UDT server got " + bytes);
		String host = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
		int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
		log.debug("UDT RemoteHost:" + host + " RemotePort:" + port);
		NetMessage gameMessage = new NetMessage(null,type, bytes, host, port,ctx);
		this.inbound.tell(gameMessage, null);
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageList<Object> msgs) {
		MessageList<UdtMessage> buffers = msgs.cast();
		for (int i = 0; i < buffers.size(); i++) {
            UdtMessage m = buffers.get(i);
            byte[] bytes = new byte[m.content().readableBytes()];
    		m.content().readBytes(bytes);
    		m.content().release();
    		sendToGateway(ctx,bytes,NetMessage.UDT);
        }
	}

	public void send(byte[] bytes, ChannelHandlerContext ctx) {

		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		UdtMessage message = new UdtMessage(buf);
		
		ctx.channel().write(message);
		log.debug("UDT server sent " + new String(bytes) + " " + bytes.length);
		//buf.release();
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		//log.info("close the connection when an exception is raised", cause);
		log.info("close the connection when an exception is raised");
		sendToGateway(ctx,null,NetMessage.DISCONNECTED);
		ctx.close();
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		log.debug("UDT server active "+ NioUdtProvider.socketUDT(ctx.channel()).toStringOptions());
	}

}

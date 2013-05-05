package com.game_machine.net.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.udt.nio.NioUdtProvider;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import com.game_machine.ActorUtil;
import com.game_machine.NetMessage;
import com.game_machine.ProtobufMessages.ClientMessage;
import com.game_machine.game.Inbound;
import com.game_machine.game.Outbound;
import com.google.protobuf.InvalidProtocolBufferException;

@Sharable
public class UdtServerHandler extends ChannelInboundMessageHandlerAdapter<ClientMessage> {

	private static final Logger log = Logger.getLogger(UdtServerHandler.class.getName());
	private UdtServer server;
	private int messageCount = 0;
	private ChannelHandlerContext ctx = null;
	private ActorSelection inbound;
	
	public UdtServerHandler() {
		log.setLevel(UdtServer.logLevel);
		this.inbound = ActorUtil.getSelectionByClass(Inbound.class);
	}
	
	public void setServer(UdtServer server) {
		this.server = server;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final ClientMessage m) {
		String str = new String(m.getBody().toByteArray());
		messageCount++;
		log.info("SERVER messageReceived " + messageCount + " " + str);
		String host = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
	    int port = ((InetSocketAddress)ctx.channel().remoteAddress()).getPort();
	    
		NetMessage gameMessage = new NetMessage(NetMessage.TCP,NetMessage.ENCODING_PROTOBUF,m.getBody().toByteArray(),host, port);
		this.inbound.tell(gameMessage, null);
		
		if (str.equals("QUIT")) {
			log.warning("QUIT RECEVIED FROM CLIENT");
			stop();
		} else {
			ActorUtil.getSelectionByClass(Outbound.class).tell(str, null);
			ctx.write(m);
		}
	}

	public void send(byte[] bytes, String host, int port) {
		ClientMessage clientMessage = null;
		try {
			clientMessage = ClientMessage.parseFrom(bytes);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		if (this.ctx.channel().isActive() == true) {
			
			this.ctx.channel().write(clientMessage);
		} else {
			log.warning("Client disconnected from server " + this.ctx.channel().remoteAddress());
		}
		
	}
	
	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		log.log(Level.WARNING, "close the connection when an exception is raised", cause);
		ctx.close();
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		log.info("SERVER ECHO active " + NioUdtProvider.socketUDT(ctx.channel()).toStringOptions());
	}

	public void beforeAdd(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
	
	public void stop() {
		this.ctx.flush().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				server.shutdown();
			}
		});
	}

}

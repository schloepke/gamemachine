package com.game_machine.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.udt.nio.NioUdtProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

import akka.actor.ActorRef;

import com.game_machine.messages.ProtobufMessages.ClientMsg;
import com.game_machine.systems.Root;

@Sharable
public class UdtServerHandler extends ChannelInboundMessageHandlerAdapter<ClientMsg> {

	private static final Logger log = Logger.getLogger(UdtServerHandler.class.getName());
	private UdtServer server;
	private int messageCount = 0;
	private ChannelHandlerContext ctx = null;

	public UdtServerHandler() {
		log.setLevel(UdtServer.logLevel);
	}
	
	public void setServer(UdtServer server) {
		this.server = server;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final ClientMsg m) {
		String str = new String(m.getBody().toByteArray());
		messageCount++;
		log.info("SERVER messageReceived " + messageCount + " " + str);

		if (str.equals("QUIT")) {
			log.warning("QUIT RECEVIED FROM CLIENT");
			stop();
		} else {
			if (Root.isRunning()) {
				ActorRef ref = Root.system.actorFor("/user/root/outbound");
				ref.tell(str, ref);
			}
			ctx.write(m);
		}
	}

	public void sendMessage(String message) {
		
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
				server.stop();
			}
		});
	}

}

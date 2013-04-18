/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.game_machine.udt_server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.game_machine.udt_server.GameProtocol.Msg;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class ServerHandler extends ChannelInboundMessageHandlerAdapter<Msg> {

	private static final Logger log = Logger.getLogger(ServerHandler.class.getName());
	private Server server;
	private int messageCount = 0;

	public ServerHandler(Server server) {
		this.server = server;
		log.setLevel(Server.logLevel);
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final Msg m) {
		String str = new String(m.getBody().toByteArray());
		messageCount++;
		log.info("SERVER messageReceived " + messageCount + " " + str);

		if (str.equals("QUIT")) {
			log.warning("QUIT RECEVIED FROM CLIENT");
			ctx.flush().addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					// TODO Auto-generated method stub
					server.stop();
				}

			});
			
		} else {
			ctx.write(m);
			ctx.flush();
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

}

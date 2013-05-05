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
package com.game_machine.net.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.udt.nio.NioUdtProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.game_machine.ProtobufMessages.ClientMessage;
import com.google.protobuf.ByteString;

public class UdtClientHandler extends ChannelInboundMessageHandlerAdapter<ClientMessage> {

	private static final Logger log = Logger.getLogger(UdtClientHandler.class.getName());

	private ChannelHandlerContext ctx = null;
	private UdtClient client;
	private int messageCount = 0;

	//final Meter meter = Metrics.newMeter(ByteEchoClientHandler.class, "rate", "bytes", TimeUnit.SECONDS);

	public UdtClientHandler(UdtClient client) {
		this.client = client;
		log.setLevel(UdtClient.logLevel);
	}
	
	public Boolean send(byte[] bytes) {
		if (ctx == null) {
			return false;
		} else {
			ClientMessage.Builder builder = ClientMessage.newBuilder();
	        ByteString reply = ByteString.copyFrom(bytes);
	        builder.setBody(reply);
	        ctx.channel().write(builder.build());
			return true;
		}
	}
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		log.warning("CLIENT ECHO active " + NioUdtProvider.socketUDT(ctx.channel()).toStringOptions());
		this.ctx = ctx;
		this.client.callable.send("READY".getBytes());
		
	}

	public void messageReceived(final ChannelHandlerContext ctx, final ClientMessage m) {
		log.info("CLIENT messageReceived " + messageCount);
		this.client.callable.send(m.toByteArray());
		
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		log.log(Level.WARNING, "close the connection when an exception is raised", cause);
		ctx.close();
	}

}

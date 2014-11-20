package io.gamemachine.net.tcp;

import io.gamemachine.net.udp.NettyUdpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServer implements Runnable {
	static final boolean SSL = System.getProperty("nossl") != null;
	static final int PORT = Integer.parseInt(System.getProperty("port", "8463"));

	private static final Logger log = LoggerFactory.getLogger(NettyUdpServer.class);
	private static Thread serverThread;

	private static TcpServer tcpServer;

	private final String hostname;
	private final int port;

	public static TcpServer getTcpServer() {
		return tcpServer;
	}

	public static void start(String host, Integer port) {

		if (tcpServer != null) {
			return;
		}

		tcpServer = new TcpServer(host, port);
		serverThread = new Thread(tcpServer);
		serverThread.start();
	}

	public static void stop() {
		log.info("Stopping TCP server");
		if (tcpServer == null) {
			return;
		}

	}

	public TcpServer(final String hostname, final int port) {
		this.port = port;
		this.hostname = hostname;
	}

	@Override
	public void run() {
		// Configure SSL.
		final SslContext sslCtx;
		if (SSL) {
			SelfSignedCertificate ssc;
			try {
				ssc = new SelfSignedCertificate();
				sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
			} catch (CertificateException | SSLException e) {
				e.printStackTrace();
				return;
			}
		} else {
			sslCtx = null;
		}

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childOption(ChannelOption.TCP_NODELAY, true);
			b.childOption(ChannelOption.SO_REUSEADDR, true);
			
			b.handler(new LoggingHandler(LogLevel.INFO));
			b.childHandler(new TcpServerInitializer(sslCtx));

			InetSocketAddress address = new InetSocketAddress(hostname, port);
			b.bind(address).sync().channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}

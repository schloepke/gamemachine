package io.gamemachine.net.http;

import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.net.udp.NettyUdpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public final class HttpServer implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
	
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private String host;
    private int port;
    private boolean useSsl;
    private HttpServerInitializer initializer;
    private Channel channel;
    private InetSocketAddress address;
    
    public HttpServer(String host, int port, boolean useSsl,String actorName, HttpHelper httpHelper) {
    	this.host = host;
    	this.port = port;
    	this.useSsl = useSsl;
    	SslContext sslCtx = getSslContext();
    	initializer = new HttpServerInitializer(sslCtx,actorName, httpHelper);
    	this.address = new InetSocketAddress(host,port);
    }
    
    public void start() {
    	logger.info("Http Starting host "+host+" port "+port+" ssl "+useSsl);
    	executor.execute(this);
    }
    
    public void stop() {
    	channel.close();
		try {
			channel.closeFuture().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    private SslContext getSslContext() {
    	SslContext sslCtx;
        if (useSsl) {
            SelfSignedCertificate ssc;
			try {
				ssc = new SelfSignedCertificate(host);
				sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
			} catch (CertificateException | SSLException e) {
				e.printStackTrace();
				sslCtx = null;
			}
            
        } else {
            sslCtx = null;
        }
        return sslCtx;
    }
    
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(initializer);

            channel = b.bind(address).sync().channel();

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

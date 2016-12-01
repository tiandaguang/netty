package com.chat.server.example.securechat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import com.chat.server.example.telnet.TelnetClient;

/**
 * Simple SSL chat client modified from {@link TelnetClient}.
 */
public final class SecureChatClient {

	static final String HOST = System.getProperty("host", "127.0.0.1");
	static final int PORT = Integer
			.parseInt(System.getProperty("port", "8992"));

	public Channel init (String line) throws Exception {
		// Configure SSL.
		final SslContext sslCtx = SslContextBuilder.forClient()
				.trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		Channel ch = null;
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.handler(new SecureChatClientInitializer(sslCtx));

			// Start the connection attempt.
			ch = b.connect(HOST, PORT).sync().channel();
			// Read commands from the stdin.
			
			System.out.println("ch.eventLoop"+ch.eventLoop().parent());
			System.out.println("group "+group);
			System.out.println("发送之前group是否关闭"+group.isShutdown());
			// Sends the received line to the server.
			ChannelFuture lastWriteFuture = ch.writeAndFlush(line );

			System.out.println("发送之后group是否关闭"+group.isShutdown());
			// Wait until all messages are flushed before closing the channel.
			if (lastWriteFuture != null) {
				lastWriteFuture.sync();
			}
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		return ch;
	}
}

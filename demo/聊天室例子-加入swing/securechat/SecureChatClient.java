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

import java.util.HashMap;
import java.util.Map;

import com.chat.server.example.telnet.TelnetClient;

/**
 * Simple SSL chat client modified from {@link TelnetClient}.
 */
public final class SecureChatClient {

	static final String HOST = System.getProperty("host", "127.0.0.1");
	static final int PORT = Integer
			.parseInt(System.getProperty("port", "8992"));

	private Map<String, Channel> mp = new HashMap<String, Channel>();

	public void send(String str) throws Exception {
		if (mp.isEmpty() || mp.get(str) == null) {
			// Configure SSL.
			final SslContext sslCtx = SslContextBuilder.forClient()
					.trustManager(InsecureTrustManagerFactory.INSTANCE).build();

			EventLoopGroup group = new NioEventLoopGroup();
			try {
				Bootstrap b = new Bootstrap();
				b.group(group).channel(NioSocketChannel.class)
						.handler(new SecureChatClientInitializer(sslCtx));

				// Start the connection attempt.
				Channel ch = b.connect(HOST, PORT).sync().channel();
				System.out.println(ch.id());
				// Read commands from the stdin.
				ChannelFuture lastWriteFuture = null;

				// Sends the received line to the server.
				lastWriteFuture = ch.writeAndFlush(str + "\r\n");
				mp.put(str, ch);
				// Wait until all messages are flushed before closing the
				// channel.
				if (lastWriteFuture != null) {
					lastWriteFuture.sync();
				}
				// If user typed the 'bye' command, wait until the server closes
				// the connection.
				// if ("bye".equals(line.toLowerCase())) {
				// ch.closeFuture().sync();
				// }
			} finally {
				// The connection is closed automatically on shutdown.
				group.shutdownGracefully();
			}
		}else{
			Channel ch =mp.get(str);
			ch.writeAndFlush(str + "\r\n");
		}
		
	}
}

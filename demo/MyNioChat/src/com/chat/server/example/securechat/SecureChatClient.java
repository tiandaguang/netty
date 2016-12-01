package com.chat.server.example.securechat;

import io.netty.bootstrap.Bootstrap;
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


	public void init(String line) throws Exception {
		// Configure SSL.
		final SslContext sslCtx = SslContextBuilder.forClient()
				.trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.handler(new SecureChatClientInitializer(sslCtx));

			// Start the connection attempt.
			ChannelFuture future = b.connect(HOST, PORT).sync();
			// Read commands from the stdin.

			// Sends the received line to the server.
//			future = ch.writeAndFlush(line);

			if(future.isSuccess()){
				SecureChatClientHandler handler = (SecureChatClientHandler) future.channel().pipeline()
						.first();
				System.out.println(handler);
				handler.sendMsg(line);
			}
			// Wait until all messages are flushed before closing the channel.
			// if (lastWriteFuture != null) {
			// lastWriteFuture.sync();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

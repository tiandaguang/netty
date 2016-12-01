package com.chat.server.example.securechat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles a server-side channel.
 */
public class SecureChatServerHandler extends
		SimpleChannelInboundHandler<String> {

	static final ChannelGroup channels = new DefaultChannelGroup(
			GlobalEventExecutor.INSTANCE);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		// Once session is secured, send a greeting and register the channel to
		// the global channel
		// list so the channel received the messages from others.
		ctx.pipeline().get(SslHandler.class).handshakeFuture()
				.addListener(new GenericFutureListener<Future<Channel>>() {
					@Override
					public void operationComplete(Future<Channel> future)
							throws Exception {
//						ctx.writeAndFlush("Welcome to "
//								+ InetAddress.getLocalHost().getHostName()
//								+ " secure chat service!\n");
//						ctx.writeAndFlush("Your session is protected by "
//								+ ctx.pipeline().get(SslHandler.class).engine()
//										.getSession().getCipherSuite()
//								+ " cipher suite.\n");
						channels.add(ctx.channel());
					}
				});
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		printInfo("[" + ctx.channel().id().asLongText()+"---->]"+msg+"   ****  "+channels.size());
		// Send the received message to all channels but the current one.
		for (Channel c : channels) {
			 if (c != ctx.channel()) {
			 c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] "
			 + msg + '\n');
			 } else {
			 c.writeAndFlush("[you] " + msg + '\n');
			 }
		}

		// Close the connection if the client has sent 'bye'.
		if ("bye".equals(msg.toLowerCase())) {
			ctx.close();
		}
	}

	public void channelRead(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.out.println(msg+"上线");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		 super.channelReadComplete(ctx);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.channel().id().asLongText()+ "   handlerAdded ");// TODO
		super.handlerAdded(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	/***
	 * 窗口显示方法
	 * 
	 * @param str
	 */
	private void printInfo(String str) {
		System.out.println("[" + sdf.format(new Date()) + "] -> " + str);
	}

}

package com.chat.server.example.securechat;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Handles a client-side channel.
 */
@Sharable
public class SecureChatClientHandler extends
		SimpleChannelInboundHandler<String> {
	private ChannelHandlerContext ctx;

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.err.println("service said:" + msg);
		sendMsg(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive");
		this.ctx = ctx;
	}

	public void sendMsg(String msg){
		ctx.writeAndFlush(msg);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive");
		super.channelInactive(ctx);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("handlerAdded");
		super.handlerAdded(ctx);
	}
}
package com.chat.server.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	/**
	 * 此方法会在连接到服务器后被调用
	 * */
	public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("EchoClientHandler#channelActive");
		String msg = "Are you ok?";  
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());  
        encoded.writeBytes(msg.getBytes());  
        ctx.write(encoded);  
        ctx.flush();  
	}

	/**
	 * 此方法会在接收到服务器数据后调用
	 * */
	public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
		System.out.println("Client received: "
				+ ByteBufUtil.hexDump(in.readBytes(in.readableBytes())));
	}

	/**
	 * 捕捉到异常
	 * */
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf result = (ByteBuf) msg;
		byte[] result1 = new byte[result.readableBytes()];
		result.readBytes(result1);
		System.out.println("Server said:" + new String(result1));
		result.release();
	}

}
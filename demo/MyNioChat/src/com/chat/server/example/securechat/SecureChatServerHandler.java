package com.chat.server.example.securechat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
   * Handles a server-side channel.
   */
  public class SecureChatServerHandler extends SimpleChannelInboundHandler<String> {
  
      static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  
      @Override
      public void channelActive( ChannelHandlerContext ctx) {
    	  channels.add(ctx.channel());
          // Once session is secured, send a greeting and register the channel to the global channel
      }
  
      
      
      @Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			// TODO Auto-generated method stub
//			super.channelRead(ctx, msg);
    	  System.out.println(msg);
		}



	@Override
      public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
          // Send the received message to all channels but the current one.
          for (Channel c: channels) {
              if (c != ctx.channel()) {
                  c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] " + msg + '\n');
              } else {
                  c.writeAndFlush("[you] " + msg + '\n');
              }
          }
  
          // Close the connection if the client has sent 'bye'.
          if ("bye".equals(msg.toLowerCase())) {
              ctx.close();
          }
      }
  
      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
          cause.printStackTrace();
          ctx.close();
      }



	@Override
	public boolean acceptInboundMessage(Object msg) throws Exception {
		System.out.println("acceptInboundMessage");
		return super.acceptInboundMessage(msg);
	}



	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelRegistered");
		super.channelRegistered(ctx);
	}



	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelUnregistered");
		super.channelUnregistered(ctx);
	}



	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive");
		super.channelInactive(ctx);
	}



	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelReadComplete");
		super.channelReadComplete(ctx);
	}



	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		System.out.println("userEventTriggered");
		super.userEventTriggered(ctx, evt);
	}



	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx)
			throws Exception {
		System.out.println("channelWritabilityChanged");
		super.channelWritabilityChanged(ctx);
	}



	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("handlerAdded");
		super.handlerAdded(ctx);
	}



	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handlerRemoved");
		super.handlerRemoved(ctx);
	}



      
  }

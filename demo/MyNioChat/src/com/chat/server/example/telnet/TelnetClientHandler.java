package com.chat.server.example.telnet;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
 public class TelnetClientHandler extends SimpleChannelInboundHandler<String> {
 
     @Override
     protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
         System.err.println(msg);
     }
 
     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
         cause.printStackTrace();
         ctx.close();
     }
 }
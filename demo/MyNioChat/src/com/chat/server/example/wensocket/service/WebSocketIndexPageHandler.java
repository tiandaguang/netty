package com.chat.server.example.wensocket.service;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;

/**
   * Outputs index page content.
   */
  public class WebSocketIndexPageHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
  
      private final String websocketPath;
  
      public WebSocketIndexPageHandler(String websocketPath) {
          this.websocketPath = websocketPath;
      }
  
      @Override
      protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
          // Handle a bad request.
          if (!req.decoderResult().isSuccess()) {
              sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
              return;
          }
  
          // Allow only GET methods.
          if (req.method() != HttpMethod.GET) {
              sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
              return;
          }
  
          // Send the index page
          if ("/".equals(req.uri()) || "/index.html".equals(req.uri())) {
              String webSocketLocation = getWebSocketLocation(ctx.pipeline(), req, websocketPath);
              ByteBuf content = WebSocketServerIndexPage.getContent(webSocketLocation);
              FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
  
              res.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
              HttpUtil.setContentLength(res, content.readableBytes());
  
              sendHttpResponse(ctx, req, res);
          } else {
              sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
          }
      }
  
      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
          cause.printStackTrace();
          ctx.close();
      }
  
      private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
          // Generate an error page if response getStatus code is not OK (200).
          if (res.status().code() != 200) {
              ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
              res.content().writeBytes(buf);
              buf.release();
              HttpUtil.setContentLength(res, res.content().readableBytes());
          }
  
          // Send the response and close the connection if necessary.
          ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
        String protocol = "ws";
        if (cp.get(SslHandler.class) != null) {
            // SSL in use so use Secure WebSockets
            protocol = "wss";
        }
        return protocol + "://" + req.headers().get(HttpHeaderNames.HOST) + path;
    }
}

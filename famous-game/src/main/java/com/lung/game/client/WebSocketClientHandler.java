package com.lung.game.client;

import com.lung.game.bean.proto.msg.MsgPlayer;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;

@ChannelHandler.Sharable
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketClientHandshaker handShaker;
    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(WebSocketClientHandshaker handShaker) {
        this.handShaker = handShaker;
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handShaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 连接关闭处理逻辑
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object frame) {
        Channel ch = ctx.channel();
        // 处理接收到的 WebSocket 帧数据
        if (!handShaker.isHandshakeComplete()) {
            try {
                handShaker.finishHandshake(ch, (FullHttpResponse) frame);
                handshakeFuture.setSuccess();
                System.out.println("WebSocket handshake completed successfully.");

                // 在握手成功之后立即发送消息给服务器
                MsgPlayer.CSLogin.Builder loginMessage = MsgPlayer.CSLogin.newBuilder();
                loginMessage.setUid("123456");
//                TextWebSocketFrame textFrame = new TextWebSocketFrame("Hello, WebSocket Server!");
                ctx.writeAndFlush(loginMessage.build());
            } catch (WebSocketHandshakeException e) {
                handshakeFuture.setFailure(e);
                System.out.println("WebSocket handshake failed: " + e.getMessage());
            }
            return;
        }

        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            System.out.println("Received message: " + textFrame.text());
        }
        // 其他类型的帧处理
        // ...
        if (frame instanceof FullHttpResponse) {
            // 处理响应消息
        } else if (frame instanceof WebSocketFrame) {
            // 处理 WebSocket 帧消息
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理逻辑
    }
}

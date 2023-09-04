package com.lung.game.client;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 发送心跳消息
            // 这里可以根据实际需要定义心跳消息内容和格式
            String heartbeatMessage = "Heartbeat";
            ctx.writeAndFlush(heartbeatMessage);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

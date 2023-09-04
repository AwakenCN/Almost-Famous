package com.lung.game.client;

import com.lung.game.bean.proto.msg.MsgPlayer;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;

@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        MsgPlayer.CSLogin.Builder requestMessage = MsgPlayer.CSLogin.newBuilder();
        requestMessage.setUid("001");
        channel.writeAndFlush(requestMessage.build());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Channel channel = ctx.channel();
        MsgPlayer.CSLogin.Builder requestMessage = MsgPlayer.CSLogin.newBuilder();
        requestMessage.setUid("001");
        channel.writeAndFlush(requestMessage.build());
    }
}

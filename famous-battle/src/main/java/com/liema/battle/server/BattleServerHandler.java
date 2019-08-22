package com.liema.battle.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author Noseparte
 * @date 2019/8/22 12:02
 * @Description
 */
public class BattleServerHandler extends SimpleChannelInboundHandler<Protocol> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol msg) throws Exception {

    }
}

package com.noseparte.match.client;

import com.noseparte.common.battle.server.Protocol;
import com.noseparte.match.server.RequestDispatch;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends io.netty.channel.SimpleChannelInboundHandler<Protocol> {

    protected static Logger LOG = LoggerFactory.getLogger("Battle");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol msg) throws Exception {
        LOG.debug(msg.toString());
        RequestDispatch.dispatch(msg, ctx.channel().id().asShortText().hashCode());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

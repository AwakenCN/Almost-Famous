package com.liema.battle.server;

import com.liema.battle.utils.BattleMisc;
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
        int sid = BattleMisc.getSid(ctx.channel());

        int type = msg.getType();
        if(!BattleMisc.isCheckLoginProtocol(type)){
            RequestDispatch.dispatch(msg, sid);
        }
    }
}

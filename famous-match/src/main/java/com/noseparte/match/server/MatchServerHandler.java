package com.noseparte.match.server;

import com.noseparte.common.battle.server.ConnState;
import com.noseparte.common.battle.server.LinkMgr;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.common.battle.server.Session;
import com.noseparte.common.global.Misc;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Auther: Noseparte
 * @Date: 2020/3/9 18:41
 * @Version 1.0
 * @Description: 
 *
 *          <p>Match Server Handler</p>
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MatchServerHandler extends SimpleChannelInboundHandler<Protocol> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol msg) {
        int sid = Misc.getSid(ctx.channel());

        int type = msg.getType();
        if (!Misc.isCheckLoginProtocol(type)) {
            RequestDispatch.dispatch(msg, sid);
            return;
        }

        Session session = LinkMgr.getSession(sid);
        if (session == null) {
            log.error("unlogin conneted.");
            LinkMgr.closeOnFlush(ctx.channel(), ConnState.CLOSE_NOT_LOGIN);
            return;
        }

        RequestDispatch.dispatch(msg, sid);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (log.isDebugEnabled()) {
            log.debug("接入连接{}", Misc.getSid(ctx.channel()));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (log.isDebugEnabled()) {
            log.debug("断开连接{}", Misc.getSid(ctx.channel()));
        }
        LinkMgr.closeOnFlush(ctx.channel(), ConnState.CLIENT_CLOSE);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //超时事件
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleEvent = (IdleStateEvent) evt;
            //读
            if (idleEvent.state() == IdleState.READER_IDLE) {
                LinkMgr.closeOnFlush(ctx.channel(), ConnState.CLOSE_HEARTBEAT_EXPIRE);
            }
            //写
            else if (idleEvent.state() == IdleState.WRITER_IDLE) {

            }
            //全部
            else if (idleEvent.state() == IdleState.ALL_IDLE) {

            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        if (log.isDebugEnabled()) {
            log.debug("异常的连接{}", Misc.getSid(ctx.channel()));
        }
        LinkMgr.closeOnFlush(ctx.channel(), ConnState.CLOSE_EXCEPTION);
    }
}

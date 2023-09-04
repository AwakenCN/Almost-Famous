package com.lung.server.handler;

import com.alibaba.fastjson2.JSON;
import com.lung.FamousGameApplication;
import com.lung.game.bean.GameRequest;
import com.lung.game.bean.SessionKey;
import com.lung.game.manager.SessionManager;
import com.lung.game.thread.MessageTask;
import com.lung.utils.ChannelUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author haoyitao
 * @implSpec 自定义拦截器
 * @since 2023/8/30 - 15:15
 * @version 1.0
 */
@ChannelHandler.Sharable
public class ServerWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ServerWebSocketHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        Channel channel = channelHandlerContext.channel();
        if (msg instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) msg).text();
            logger.info("接收到通道消息：{}， body：{}", channel.id().asShortText().hashCode(), text);
            GameRequest request = JSON.parseObject(text, GameRequest.class);
            parseMsg(channel, request);
            MessageTask task = new MessageTask(channel, request);
            FamousGameApplication.gameThreadPoolManager.execute(task);
        }
    }

    private void parseMsg(Channel channel, GameRequest request) {
        request.setChannel(channel);
        //保存token
        ChannelUtils.setAttr(channel, SessionKey.TOKEN, request.getToken());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SessionManager.getInstance().addSession(channel);
        logger.info("有新的连接接入：{}, channel state: {}", channel.id().asShortText().hashCode(), channel.isOpen());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SessionManager.getInstance().removeSession(channel);
        logger.info("连接关闭：{}, channel state: {}", channel.id().asShortText().hashCode(), channel.isOpen());
    }

}

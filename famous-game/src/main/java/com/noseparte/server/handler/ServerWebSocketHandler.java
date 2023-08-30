package com.noseparte.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
        logger.info("接收到通道消息：{}， body：{}", channel.id().asShortText().hashCode(), msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        logger.info("有新的连接接入：{}, channel state: {}", channel.id().asShortText().hashCode(), channel.isOpen());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        logger.info("连接关闭：{}, channel state: {}", channel.id().asShortText().hashCode(), channel.isOpen());
    }

}

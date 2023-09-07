package com.lung.server.handler;

import com.alibaba.fastjson2.JSON;
import com.lung.FamousGameApplication;
import com.lung.game.bean.GameRequest;
import com.lung.game.bean.SessionKey;
import com.lung.game.manager.SessionManager;
import com.lung.game.thread.MessageTask;
import com.lung.utils.ChannelUtils;
import com.lung.utils.TraceUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author haoyitao
 * @implSpec 自定义拦截器
 * @since 2023/8/30 - 15:15
 * @version 1.0
 */
@ChannelHandler.Sharable
public class ServerWebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(ServerWebSocketHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame frame) throws Exception {
        Channel channel = channelHandlerContext.channel();
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            logger.info("接收到通道消息：{}， body：{}", channel.id().asShortText().hashCode(), text);
            GameRequest request = JSON.parseObject(text, GameRequest.class);
            parseMsg(channel, request);
            MessageTask task = new MessageTask(channel, request);
            FamousGameApplication.gameThreadPoolManager.execute(task);
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) frame;
            // 处理逻辑...
        } else if (frame instanceof PingWebSocketFrame) {
            // 处理心跳...
            logger.info("接收到心跳消息：PING");
        } else if (frame instanceof PongWebSocketFrame) {
            // 处理心跳...
            logger.info("接收到心跳消息：PONG");
        } else if (frame instanceof CloseWebSocketFrame) {
            // 处理连接关闭...
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String traceInfo = TraceUtils.getTraceInfo(cause);
        logger.error("exceptionCaught, {}, {}", ctx.channel().id().asShortText().hashCode(), traceInfo);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 心跳检测
        logger.info("channel {}, 服务端收到心跳信息, {}", ctx.channel().id().asShortText().hashCode(), evt.toString());
    }
}

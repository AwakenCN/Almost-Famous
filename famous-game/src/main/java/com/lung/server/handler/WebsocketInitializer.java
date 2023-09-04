package com.lung.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author haoyitao
 * @version 1.0
 * @implSpec 给pipeline添加handler
 * @since 2023/8/29 - 18:26
 */
@ChannelHandler.Sharable
public class WebsocketInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 设置 ChannelOption 参数
        channel.config().setOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        channel.config().setOption(ChannelOption.SO_KEEPALIVE, true);
        channel.config().setOption(ChannelOption.SO_REUSEADDR, true);

        ChannelPipeline pipeline = channel.pipeline();
        // 添加 WebSocket 相关处理器和拦截器
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(512 * 1024));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new IdleStateHandler(30, 30, 30));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        // ... 其他设置
        pipeline.addLast(new ServerWebSocketHandler());
    }
}

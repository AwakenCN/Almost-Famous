package com.lung.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.ssl.SslContext;
import java.util.concurrent.TimeUnit;

/**
 * @author haoyitao
 * @version 1.0
 * @implSpec 给pipeline添加handler
 * @since 2023/8/29 - 18:26
 */
public class WebsocketInitializer extends ChannelInitializer<SocketChannel> {

    private static final String WEBSOCKET_PATH = "/ws";

    private final SslContext sslContext;

    public WebsocketInitializer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 设置 ChannelOption 参数
        channel.config().setOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        channel.config().setOption(ChannelOption.SO_KEEPALIVE, true);
        channel.config().setOption(ChannelOption.SO_REUSEADDR, true);

        ChannelPipeline pipeline = channel.pipeline();
        if (null != sslContext) {
            pipeline.addLast(sslContext.newHandler(channel.alloc()));
        }
        // 添加 WebSocket 相关处理器和拦截器
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(512 * 1024));
//        pipeline.addLast(new WebSocket13FrameDecoder(true, true, 1024*1024));
//        pipeline.addLast(new WebSocket13FrameEncoder(true));
//        pipeline.addLast(new ProtobufVarint32FrameDecoder());
//        pipeline.addLast(new ProtobufDecoder(MsgPlayer.CSLogin.getDefaultInstance()));
//
//        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
//        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast(new WebSocketServerCompressionHandler());

        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
//        pipeline.addLast(new WebSocketIndexPageHandler(WEBSOCKET_PATH));
        pipeline.addLast(new IdleStateHandler(5, 5, 60, TimeUnit.SECONDS));
        // ... 其他设置
        pipeline.addLast(new ServerWebSocketHandler());

    }
}

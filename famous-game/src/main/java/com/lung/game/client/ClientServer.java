package com.lung.game.client;

import com.lung.game.bean.proto.msg.MsgPlayer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;

/**
 * @author haoyitao
 * @implSpec
 * @since 2023/9/4 - 17:37
 * @version 1.0
 */
public class ClientServer {


    public static void main(String[] args) {
        WebSocketClientHandshaker handShaker = WebSocketClientHandshakerFactory.newHandshaker(
                null, WebSocketVersion.V13, null, false,
                null);

        Bootstrap b = new Bootstrap();
        EventLoopGroup workGroup = new NioEventLoopGroup(5);
        b.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(512 * 1024));
                        pipeline.addLast(new ChunkedWriteHandler());
                        pipeline.addLast(new ClientHandler());
                        pipeline.addLast(new WebSocketClientProtocolHandler(handShaker));
                    }
                })
                ;
        try {
            ChannelFuture future = b.connect(new InetSocketAddress(8888));
            Channel channel = future.sync().channel();
            future.addListener(event -> {
                // Thread.sleep(MathUtil.getRandom(10, 1000));//模拟延迟
                MsgPlayer.CSLogin.Builder requestMessage = MsgPlayer.CSLogin.newBuilder();
                requestMessage.setUid("001");
                channel.writeAndFlush(requestMessage.build());
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}

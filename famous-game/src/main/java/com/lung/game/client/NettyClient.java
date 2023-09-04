package com.lung.game.client;

import com.lung.game.bean.proto.msg.MsgPlayer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * @author haoyitao
 * @implSpec
 * @since 2023/9/4 - 17:37
 * @version 1.0
 */
public class NettyClient {


//    public static void main(String[] args) {
//        WebSocketClientHandshaker handShaker = WebSocketClientHandshakerFactory.newHandshaker(
//                URI.create("http://127.0.0.1:8888"), WebSocketVersion.V13, null, false,
//                null);
//
//        Bootstrap b = new Bootstrap();
//        EventLoopGroup workGroup = new NioEventLoopGroup(5);
//        b.group(workGroup)
//                .channel(NioSocketChannel.class)
//                .option(ChannelOption.SO_REUSEADDR, true)
//                .option(ChannelOption.TCP_NODELAY, true)
//                .handler(new LoggingHandler(LogLevel.INFO))
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast(new HttpServerCodec());
//                        pipeline.addLast(new HttpResponseDecoder());
//                        pipeline.addLast(new HttpObjectAggregator(512 * 1024));
//                        pipeline.addLast(new ChunkedWriteHandler());
//                        pipeline.addLast(new ClientHandler());
//                        pipeline.addLast(new WebSocketClientProtocolHandler(handShaker));
//                    }
//                })
//                ;
//        try {
//            ChannelFuture future = b.connect(new InetSocketAddress(8888));
//            Channel channel = future.sync().channel();
//            future.addListener(event -> {
//                // Thread.sleep(MathUtil.getRandom(10, 1000));//模拟延迟
//                MsgPlayer.CSLogin.Builder requestMessage = MsgPlayer.CSLogin.newBuilder();
//                requestMessage.setUid("001");
//                channel.writeAndFlush(requestMessage.build());
//            });
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }


    private final String host;
    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 8888;

        NettyClient client = new NettyClient(host, port);
        client.run();
    }

}

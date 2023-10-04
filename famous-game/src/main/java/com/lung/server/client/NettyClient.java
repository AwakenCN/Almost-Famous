package com.lung.server.client;

import com.lung.game.entry.proto.msg.MsgPlayer;
import com.lung.utils.SslUtils;
import com.lung.utils.TraceUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * @author haoyitao
 * @version 1.0
 * @implSpec
 * @since 2023/9/4 - 17:37
 */
public class NettyClient {

    private final static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private final URI uri;
    private final EventLoopGroup group;

    static Channel channel;

    public NettyClient(URI uri) {
        this.uri = uri;
        this.group = new NioEventLoopGroup();
    }

    /**
     * WebSocket客户端的实现。它使用Netty框架来创建一个基于NIO的Socket连接，并与服务器进行WebSocket握手。然后，它通过控制台读取用户输入的消息，并将消息发送给服务器。
     * 1. 创建一个SSL上下文对象，用于与服务器建立安全连接。
     * 2. 创建一个WebSocketClientHandler对象，用于处理WebSocket握手和消息的收发。
     * 3. 创建一个Bootstrap对象，用于配置和启动客户端。
     * 4. 初始化ChannelPipeline，添加SSL处理器、HTTP编解码器、HTTP对象聚合器和WebSocket压缩处理器。
     * 5. 连接服务器并进行握手。
     * 6. 通过控制台读取用户输入的消息，并根据消息类型发送不同类型的WebSocket帧。
     * 7. 如果用户输入"bye"，则发送关闭WebSocket帧并关闭连接。
     * 8. 如果用户输入"ping"，则发送PingWebSocket帧。
     * 9. 如果用户输入其他消息，则发送TextWebSocket帧。
     * 10. 循环结束后，关闭连接并释放资源。
     *
     * @throws Exception 抛出异常
     */
    public void connect() throws Exception {
        try {
            SslContext sslContext = SslUtils.createClientSslContext();

            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
            final WebSocketClientHandler handler =
                    new WebSocketClientHandler(
                            WebSocketClientHandshakerFactory.newHandshaker(
                                    uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));

            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.TCP_NODELAY, true)
//                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
//                            p.addLast(sslContext.newHandler(ch.alloc(), uri.getHost(), uri.getPort()));
                            p.addLast(
                                    new HttpClientCodec(),
                                    new HttpObjectAggregator(8192),
                                    WebSocketClientCompressionHandler.INSTANCE,
//                                    new IdleStateHandler(5, 5, 30, TimeUnit.SECONDS),
                                    handler);
                        }

                    })
            ;

            channel = b.connect(uri.getHost(), uri.getPort()).sync().channel();
            handler.handshakeFuture().sync();
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String msg = console.readLine();
                if (msg == null) {
                    break;
                } else if ("login".equalsIgnoreCase(msg)) {
                    // 在握手成功之后立即发送消息给服务器
                    MsgPlayer.CSLogin.Builder loginMessage = MsgPlayer.CSLogin.newBuilder();
                    loginMessage.setUid("123456");
                    channel.writeAndFlush(loginMessage.build().toByteArray());
                    channel.closeFuture().sync();
                    break;
                } else if ("bye".equalsIgnoreCase(msg)) {
                    channel.writeAndFlush(new CloseWebSocketFrame());
                    channel.closeFuture().sync();
                    break;
                } else if ("ping".equalsIgnoreCase(msg)) {
                    WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
                    channel.writeAndFlush(frame);
                } else {
                    WebSocketFrame frame = new TextWebSocketFrame(msg);
                    channel.writeAndFlush(frame);
                }
            }
//            channel.closeFuture().sync();
        } catch (Exception e) {
            String traceInfo = TraceUtils.getTraceInfo(e);
            logger.error("run error, {}", traceInfo, e);
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
        //连接成功
        logger.info("client connecting server successful, {}", uri);
    }

    public static void main(String[] args) throws Exception {
        URI uri = new URI("ws://localhost:8888/ws"); // 设置 WebSocket 服务器的地址
        NettyClient client = new NettyClient(uri);
        client.connect();

        channel.writeAndFlush(new PingWebSocketFrame());

        // 在握手成功之后立即发送消息给服务器
        MsgPlayer.CSLogin.Builder loginMessage = MsgPlayer.CSLogin.newBuilder();
        loginMessage.setUid("123456");
        channel.writeAndFlush(loginMessage.build());
    }

}

package com.lung.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lung.server.handler.WebsocketInitializer;
import com.lung.utils.CommonUtils;
import com.lung.utils.SslUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.ResourceLeakDetector;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Objects;

/**
 * @author haoyitao
 * @version 1.0
 * WebSocket 的关键特点和机制包括以下几个方面：
 * 1. 双向通信：
 * WebSocket 提供全双工的双向通信，允许服务器主动向客户端推送数据，而不仅仅是客户端向服务器发起请求。这种双向实时通信的特性使得 WebSocket 适用于实时消息传递、实时数据更新等场景。
 * 2. 基于 HTTP 协议：
 * WebSocket 利用 HTTP 协议进行握手过程，借助 HTTP 的升级机制从普通的 HTTP 连接升级到 WebSocket 连接。这意味着在支持 WebSocket 的环境下，可以通过标准的 HTTP 80 端口或安全的 HTTPS 443 端口进行通信。
 * 3. 持久连接：
 * 一旦完成 WebSocket 握手，客户端和服务器之间的连接将保持打开状态，而不会像传统的 HTTP 请求那样在每次请求后关闭连接。这有助于减少每次请求的开销，提高通信的效率，并且支持服务器主动推送数据。
 * 4. 低延迟：
 * WebSocket 的持久连接机制消除了重复建立连接和断开连接的开销，因此具有较低的延迟。当数据需要实时传输时，WebSocket 可以比轮询等其他传统技术更快地响应和传输数据。
 * 5. 协议支持：
 * WebSocket 定义了一种基于 TCP 的协议，该协议具有标准化的消息格式和数据交换机制。WebSocket 可以与各种编程语言和框架兼容，使开发者能够轻松实现 WebSocket 通信。
 * 总结起来，WebSocket 的关键在于实现了双向通信、基于 HTTP 协议的握手、持久连接、低延迟以及标准化的协议支持。这些特点使得 WebSocket 成为实时通信和实时数据传输的理想选择。
 * @implSpec
 * @since 2023/8/29 - 17:38
 */
@Component
public class WebsocketServer {

    private final static Logger logger = LoggerFactory.getLogger(WebsocketServer.class);

    /**
     * @param port netty网络端口
     */
    public void start(int port) {
        //设置采样器
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
        EventLoopGroup bossGroup = new NioEventLoopGroup(new ThreadFactoryBuilder().setNameFormat("boss_").build());
        EventLoopGroup workerGroup = new NioEventLoopGroup(10, new ThreadFactoryBuilder().setNameFormat("work_").build());
        try {
            // 1. 准备 Keystore：
            // 2. 创建 KeyManagerFactory：
            // 3. 创建 TrustManagerFactory（用于服务器验证客户端证书）：
            // 4. 创建 SslContext：
            SslContext sslContext = SslUtils.createServerSslContext();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(workerGroup, bossGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 12800)
//                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120)
//                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_RCVBUF, 10485760)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new WebsocketInitializer(sslContext))
//                    .childOption(ChannelOption.TCP_NODELAY, true)
            ;

            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            String errorInfo = stackTrace[0].getClassName() + stackTrace[0].getFileName() + stackTrace[0].getLineNumber();
            logger.error("netty network start error, {}", errorInfo, e);
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
        logger.info("netty server starting");
    }

}

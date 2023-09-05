package com.lung.game.client;

import com.lung.utils.CommonUtils;
import com.lung.utils.TraceUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    public NettyClient(URI uri) {
        this.uri = uri;
        this.group = new NioEventLoopGroup();
    }


    public void connect() throws Exception {
        try {
            // 1. 准备 Keystore：
            String fileName = "/almost-famous.keystore"; // CA 证书文件路径
            File file = new File(CommonUtils.getClassPath() + fileName);
            String keystorePath = file.getPath();  // Keystore 文件路径
            logger.info("ssl 证书路径：{}", keystorePath);
            String keystorePassword = "noseparte";  // Keystore 密码

            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (InputStream inputStream = Files.newInputStream(Paths.get(keystorePath))) {
                keyStore.load(inputStream, keystorePassword.toCharArray());
            }

            // 2. 创建 KeyManagerFactory：
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keystorePassword.toCharArray());


            // 3. 创建 TrustManagerFactory（用于服务器验证客户端证书）：
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            // 4. 创建 SslContext：
            SslContext sslContext = SslContextBuilder.forClient().trustManager(tmf).build();

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
                            if (sslContext != null) {
                                p.addLast(sslContext.newHandler(ch.alloc(), uri.getHost(), uri.getPort()));
                            }
                            p.addLast(
                                    new HttpClientCodec(),
                                    new HttpObjectAggregator(8192),
                                    WebSocketClientCompressionHandler.INSTANCE,
                                    handler);
                        }

                    })
            ;

            Channel channel = b.connect(uri.getHost(), uri.getPort()).sync().channel();
            handler.handshakeFuture().sync();
            channel.closeFuture().sync();
        } catch (Exception e) {
            String traceInfo = TraceUtils.getTraceInfo(e);
            logger.error("run error, {}", traceInfo, e);
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        URI uri = new URI("wss://localhost:8888/ws"); // 设置 WebSocket 服务器的地址
        NettyClient client = new NettyClient(uri);
        client.connect();
    }

}

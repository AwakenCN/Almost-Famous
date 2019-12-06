package com.noseparte.match.server;

import com.noseparte.match.MatchServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MatchServer {
    Object waitLock = new Object();

    @Autowired
    MatchServerConfig matchServerConfig;

    private static EventLoopGroup boss = new NioEventLoopGroup();
    private static EventLoopGroup work = new NioEventLoopGroup();
    private static ServerBootstrap b = new ServerBootstrap();

    @Autowired
    private MatchServerInitializer battleServerInitializer;

    public void close() {
        synchronized (waitLock) {
            log.info("关闭服务器....");
            //优雅退出
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    public void start() {
        synchronized (waitLock) {
            int port = matchServerConfig.getPort();
            try {
                b.group(boss, work);
                b.channel(NioServerSocketChannel.class);
                b.childHandler(battleServerInitializer);
                log.info("匹配服务器在[{}]端口启动监听", port);
                ChannelFuture f = b.bind(port).sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("[出现异常] 释放资源", e);
            } finally {
                work.shutdownGracefully();
                boss.shutdownGracefully();
            }
        }
    }

}

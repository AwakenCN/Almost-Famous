package com.noseparte.battle.server;

import com.noseparte.battle.BattleServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Noseparte
 * @date 2019/8/21 16:52
 * @Description
 */
@Slf4j
@Component
public class BattleServer {

    Object waitLock = new Object();

    private static EventLoopGroup boss = new NioEventLoopGroup();
    private static EventLoopGroup work = new NioEventLoopGroup();

    private static ServerBootstrap server = new ServerBootstrap();

    @Autowired
    BattleServerInitializer battleServerInitializer;
    @Autowired
    BattleServerConfig battleServerConfig;

    public void start() {
        synchronized (waitLock) {
            int port = battleServerConfig.getPort();
            try {
                server.group(boss, work);
                server.channel(NioServerSocketChannel.class);
                server.childHandler(battleServerInitializer);
                log.info("匹配服务器在[{}]端口启动监听", port);
                ChannelFuture future = server.bind(port);
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("[出现异常] 释放资源", e);
            }finally {
                boss.shutdownGracefully();
                work.shutdownGracefully();
            }
        }
    }

    public void close(){
        synchronized (waitLock) {
            log.info("关闭服务器....");
            //优雅退出
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }


}

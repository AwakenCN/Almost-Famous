package com.noseparte.match.server;

import com.noseparte.common.battle.server.Decoder;
import com.noseparte.common.battle.server.Encoder;
import com.noseparte.match.MatchServerConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MatchServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private MatchServerHandler matchServerHandler;
    @Autowired
    private MatchServerConfig matchServerConfig;

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline cp = sc.pipeline();
        cp.addLast(new IdleStateHandler(matchServerConfig.getHeartBeatTime(), 0, 0, TimeUnit.SECONDS));// 心跳
        cp.addLast(new LoggingHandler(LogLevel.INFO));
        cp.addLast(new Decoder());// 解码
        cp.addLast("matchServerHandler", this.matchServerHandler);
        cp.addLast(new Encoder());// 编码
    }
}

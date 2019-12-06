package com.noseparte.battle.server;

import com.noseparte.battle.BattleServerConfig;
import com.noseparte.common.battle.server.Decoder;
import com.noseparte.common.battle.server.Encoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Noseparte
 * @date 2019/8/21 17:36
 * @Description
 */
@Component
public class BattleServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    BattleServerConfig battleServerConfig;
    @Autowired
    BattleServerHandler battleServerHandler;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline cp = ch.pipeline();
        cp.addLast("heartbeatHandler", new IdleStateHandler(battleServerConfig.getHeartBeatTime(), 0, 0, TimeUnit.SECONDS));
        cp.addLast("loggingHandler", new LoggingHandler(LogLevel.INFO));
        cp.addLast("decoder", new Decoder());
        cp.addLast("battleServerHandler", this.battleServerHandler);
        cp.addLast("encoder", new Encoder());
    }


}

package com.noseparte.game.thread;

import com.noseparte.game.bean.GameRequest;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noseparte
 * @implSpec 
 * @since 2023/8/31 - 21:25
 * @version 1.0
 */
@Getter
@Setter
public class MessageTask implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(MessageTask.class);

    private Channel channel;
    private GameRequest gameRequest;

    public MessageTask(Channel channel, GameRequest gameRequest) {
        this.channel = channel;
        this.gameRequest = gameRequest;
    }

    @Override
    public void run() {
        doTask();
    }

    private void doTask() {
        logger.info("do MessageTask! {}", gameRequest);
        channel.writeAndFlush(new TextWebSocketFrame("收到消息, 请稍后"));
    }


}

package com.lung.game.thread;

import com.google.common.base.Strings;
import com.google.protobuf.GeneratedMessageV3;
import com.lung.game.bean.GameRequest;
import com.lung.game.bean.User;
import com.lung.game.bean.proto.msg.MsgHandler;
import com.lung.game.bean.proto.msg.MsgIds;
import com.lung.game.manager.MessageManager;
import com.lung.game.manager.SessionManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
//        int msgId = gameRequest.getMsgId();
//        GeneratedMessageV3 message;
//        try {
//            message = MsgIds.parseFrom(msgId, gameRequest.getParams().getBytes());
//        } catch (IOException e) {
//            logger.error("parse message error, msgId = {}", msgId, e);
//            throw new RuntimeException(e);
//        }
//        User user = SessionManager.getInstance().getUser(channel);
//        if (null == user) {
//            return;
//        }
//        MsgHandler.getInstance().handle(msgId, message, user);
        channel.writeAndFlush(new TextWebSocketFrame("收到消息, 请稍后"));
    }


}

package com.noseparte.game.manager;

import com.noseparte.game.bean.proto.msg.MsgHandler;

public class MessageManager {

    private final static MessageManager messageFactory = new MessageManager();

    public static MessageManager getInstance() {
        return messageFactory;
    }

}

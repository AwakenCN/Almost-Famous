package com.lung.game.handler;

import com.lung.server.memory.User;
import com.lung.game.entry.proto.msg.MsgPlayer;
import com.lung.game.entry.proto.msg.MsgReceiver;
import com.lung.game.entry.proto.msg.MsgReceiverHandler;
import com.lung.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;

@MsgReceiverHandler
public class PlayerHandler {

    @Autowired()
    PlayerService playerService;

    @MsgReceiver({MsgPlayer.CSLogin.class})
    public void handle(User user, MsgPlayer.CSLogin msg) {
        playerService.login(user, msg);
    }


}

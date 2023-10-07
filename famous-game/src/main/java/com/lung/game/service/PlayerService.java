package com.lung.game.service;

import com.lung.game.proto.msg.MsgPlayer;
import com.lung.server.memory.User;

public interface PlayerService {
    void login(User user, MsgPlayer.CSLogin msg);
}

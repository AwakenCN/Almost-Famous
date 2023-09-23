package com.lung.game.service;

import com.lung.server.memory.User;
import com.lung.game.bean.proto.msg.MsgPlayer;

public interface PlayerService {
    void login(User user, MsgPlayer.CSLogin msg);
}

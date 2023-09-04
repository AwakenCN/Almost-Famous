package com.lung.game.service;

import com.lung.game.bean.User;
import com.lung.game.bean.proto.msg.MsgPlayer;

public interface PlayerService {
    void login(User user, MsgPlayer.CSLogin msg);
}

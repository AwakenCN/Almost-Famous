package com.lung.game.service.impl;

import com.lung.game.bean.User;
import com.lung.game.bean.proto.msg.MsgPlayer;
import com.lung.game.service.PlayerService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class PlayerServiceImpl implements PlayerService {

    @Override
    public void login(User user, MsgPlayer.CSLogin msg) {
        System.out.println("你好");
    }
}

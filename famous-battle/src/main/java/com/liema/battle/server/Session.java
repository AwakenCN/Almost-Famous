package com.liema.battle.server;

import io.netty.channel.Channel;
import lombok.Data;

@Data
public class Session {
    long userId;
    long roleId;
    String token;
    byte isLogin;  // 0、未登录，1、已登录
    Channel channel;
    int sid;// 连接id
    int schoolId;
    long groupId;
}

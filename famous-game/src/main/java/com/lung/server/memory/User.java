package com.lung.server.memory;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haoyitao
 * @implSpec 玩家Session数据，只存在内存中，不持久化，不跨服
 * @since 2023/9/1 - 17:29
 * @version 1.0
 */
@Getter
@Setter
public class User {

    private String uid;
    private Channel channel;
    private String token;
    private String name;
    private String ip;
    private boolean isDirty;

}

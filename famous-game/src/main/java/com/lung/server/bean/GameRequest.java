package com.lung.server.bean;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author haoyitao
 * @implSpec 消息
 * @since 2023/8/31 - 17:09
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class GameRequest {

    private int msgId;
    private Channel channel;
    private String params;
    private String token;

}

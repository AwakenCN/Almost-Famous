package com.liema.battle.server;

import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Noseparte
 * @date 2019/8/22 12:04
 * @Description
 */
@Slf4j
@Data
public class Protocol {

    protected int type;
    protected byte[] msg;
    protected Channel channel;



}

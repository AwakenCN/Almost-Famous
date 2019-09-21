package com.noseparte.battle.utils;

import LockstepProto.NetMessage;
import io.netty.channel.Channel;

/**
 * @author Noseparte
 * @date 2019/8/23 15:49
 * @Description
 */
public class BattleMisc {

    public static int getSid(Channel channel) {
        return channel.id().asShortText().hashCode();
    }

    public static boolean isCheckLoginProtocol(int type) {
        if (type == NetMessage.C2S_HeartBeat_VALUE ||
                type == NetMessage.C2S_Reconnect_VALUE ||
                type == NetMessage.C2S_Match_VALUE) {
            return true;
        }
        return false;
    }
}

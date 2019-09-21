package com.noseparte.battle.server;

import com.noseparte.battle.utils.BattleMisc;
import com.noseparte.common.exception.UnloginSessionException;
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
public abstract class Protocol implements Runnable {

    protected int type;
    protected byte[] msg;
    protected Channel channel;

    protected abstract void process() throws Exception;

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    protected Session getSession() {
        int sid = BattleMisc.getSid(channel);
        Session session = LinkMgr.getSession(sid);
        return session;
    }

    protected int getSid() {
        return BattleMisc.getSid(channel);
    }

    /**
     * 不检查登录的协议里调用这个方法是获得不到roleId的.
     */
    protected long getRoleId() throws UnloginSessionException {
        if (!BattleMisc.isCheckLoginProtocol(type)) {
            throw new UnloginSessionException("{HeartBeat , Reconnect, Match} don't invoke this method.");
        }
        Session session = this.getSession();
        return session.getRoleId();
    }


}

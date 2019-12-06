package com.noseparte.common.battle.server;

import com.noseparte.common.exception.UnloginSessionException;
import com.noseparte.common.global.Misc;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


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
        int sid = Misc.getSid(channel);
        Session session = LinkMgr.getSession(sid);
        return session;
    }

    protected int getSid(){
        return Misc.getSid(channel);
    }

    /**
     * 不检查登录的协议里调用这个方法是获得不到roleId的.
     */
    protected long getRoleId() throws UnloginSessionException {
        if (!Misc.isCheckLoginProtocol(type)) {
            throw new UnloginSessionException("{HeartBeat , Reconnect, Match} don't invoke this method.");
        }
        Session session = this.getSession();
        return session.getRid();
    }



}

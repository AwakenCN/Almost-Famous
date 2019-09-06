package com.liema.battle.server;

import com.liema.battle.match.MatchMgr;
import com.liema.battle.utils.ConnState;
import com.liema.common.utils.SpringContextUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LinkMgr {

    private static Map<Integer, Session> sessionBySidMap = new ConcurrentHashMap<>();
    private static Map<Long, Integer> sessionByRoleIdMap = new ConcurrentHashMap<>();

    public static void addSession(Session session) {
        sessionBySidMap.put(session.getSid(), session);
        sessionByRoleIdMap.put(session.getRoleId(), session.getSid());
    }

    public static void removeSession(int sid) {
       Session session = sessionBySidMap.remove(sid);
       if (session != null)
           sessionByRoleIdMap.remove(session.getRoleId());
    }

    public static Session getSession(int sid) {
        return sessionBySidMap.get(sid);
    }

    public static Session getSession(Long roleId) {
        if (!sessionByRoleIdMap.containsKey(roleId))
            return null;
        int sid = sessionByRoleIdMap.get(roleId);
        return sessionBySidMap.get(sid);
    }

    public static boolean containsSid(int sid) {
        return sessionBySidMap.containsKey(sid);
    }

    public static void closeOnFlush(Channel ch, ConnState reason) {
        int sid = ch.id().asShortText().hashCode();
        MatchMgr matchMgr = SpringContextUtils.getBean("matchMgr", MatchMgr.class);
        matchMgr.remove(sid, MatchMgr.MatchEnum.DISCONNECT);
        if (ch.isActive() || containsSid(sid)) {
            if (log.isInfoEnabled()) {
                log.info("从连接管理中移除channel={}, sid={}, 断开原因={}", ch, sid, reason);
            }
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            removeSession(sid);
        }
    }

    public static Channel channelValidator(Session session, int sid) {
        Channel channel = session.getChannel();
        if (null == channel || !channel.isOpen()) {
            removeSession(sid);
            log.error("sessin={} is close!", sid);
            return null;
        }
        return channel;
    }

    public static void send(int sid, Protocol protocol) {
        Session session = getSession(sid);
        if (null == session)
            return;
        Channel channel = channelValidator(session, sid);
        if (null == channel)
            return;
        channel.writeAndFlush(protocol);
    }

    /**
     * 未登录时候发送使用
     */
    public static void send(Channel channel, Protocol protocol) {
        if (!channel.isActive())
            return;
        if (null == channel)
            return;
        channel.writeAndFlush(protocol);
    }


}

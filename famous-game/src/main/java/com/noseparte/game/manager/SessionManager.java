package com.noseparte.game.manager;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haoyitao
 * @implSpec channel session 管理
 * @since 2023/8/31 - 10:23
 * @version 1.0
 */
public class SessionManager {

    private static final SessionManager sessionManager = new SessionManager();

    public static SessionManager getInstance() {
        return sessionManager;
    }

    Map<Integer, Channel> sessionMap = new HashMap<>();

    public void addSession(Channel channel) {
        sessionMap.put(channel.id().asShortText().hashCode(), channel);
    }

    public void removeSession(Channel channel) {
        sessionMap.remove(channel.id().asShortText().hashCode());
    }
}

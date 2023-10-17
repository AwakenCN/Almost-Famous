package com.lung.game.manager;

import com.lung.server.memory.User;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author noseparte
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
    Map<Channel, User> userSessionMap = new HashMap<>();

    public void addSession(Channel channel) {
        sessionMap.put(channel.id().asShortText().hashCode(), channel);
    }

    public void removeSession(Channel channel) {
        sessionMap.remove(channel.id().asShortText().hashCode());
    }

    public void addUser(Channel channel, User user) {
        userSessionMap.put(channel, user);
    }

    public void removeUser(Channel channel) {
        userSessionMap.remove(channel);
    }

    public User getUser(Channel channel) {
        return userSessionMap.getOrDefault(channel, null);
    }
}

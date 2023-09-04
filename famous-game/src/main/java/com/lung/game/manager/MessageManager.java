package com.lung.game.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageManager {

    private final static MessageManager messageFactory = new MessageManager();

    public static MessageManager getInstance() {
        return messageFactory;
    }

    private final Map<Integer, Class<?>> handlers = new ConcurrentHashMap<>();

    public void addHandler(Integer handlerKey, Class<?> handlerClass) {
        this.handlers.put(handlerKey, handlerClass);
    }

    public synchronized void clearAll() {
        this.handlers.clear();
    }

    public synchronized void removeHandler(int handlerKey) {
        this.handlers.remove(handlerKey);
    }

    public Object findHandler(int msgId) throws InstantiationException, IllegalAccessException {
        Object handler = this.getHandlerInstance(msgId);
        if (handler == null) {
            handler = this.getHandlerInstance(msgId);
        }
        return handler;
    }

    private Object getHandlerInstance(int msgId) throws InstantiationException, IllegalAccessException {
        Class<?> handlerClass = this.handlers.get(msgId);
        try {
            return handlerClass == null ? null : handlerClass.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

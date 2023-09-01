package com.noseparte.game.bean.proto.msg;

import com.noseparte.utils.PackageClass;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.noseparte.game.bean.User;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generated automaticly, do not change.
 */
public class MsgHandler {

    private MsgHandler() {
    }

    private static class Singleton {
        private static final MsgHandler INSTANCE = new MsgHandler();
    }

    public static MsgHandler getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * 所有处理消息的方法
     */
    private Map<Integer, MsgReceiverFuntion<User, Message>> receivers = new HashMap<>();

    /**
     * 初始化消息处理器
     *
     * @param pkgName
     */
    public void init(String pkgName) throws Throwable {
        Set<Class<?>> clazzes = PackageClass.getPackageClasses(pkgName);
        for (Class<?> clazz : clazzes) {
            if (!clazz.isAnnotationPresent(MsgReceiverHandler.class)) {
                continue;
            }

            Object obj = clazz.newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                if (!m.isAnnotationPresent(MsgReceiver.class)) {
                    continue;
                }

                MsgReceiver mr = m.getAnnotation(MsgReceiver.class);
                for (Class<? extends Message> c : mr.value()) {
                    int msgId = MsgIds.getIdByClass(c);
                    receivers.put(msgId, getMsgReceiver(MethodHandles.lookup(), obj, m, c));
                }
            }
        }
    }


    private MsgReceiverFuntion<User, Message> getMsgReceiver(MethodHandles.Lookup lookup, Object annotated, Method method, Class<? extends Message> mc) throws Throwable {
        MethodHandle handle = lookup.unreflect(method);
        final CallSite site = LambdaMetafactory.metafactory(lookup, "accept",
                MethodType.methodType(MsgReceiverFuntion.class, annotated.getClass()),
                MethodType.methodType(void.class, Object.class, Object.class),
                handle,
                MethodType.methodType(void.class, User.class, mc));
        return (MsgReceiverFuntion<User, Message>) site.getTarget().invoke(annotated);
    }

    /**
     * 处理客户端发送过来的消息
     *
     * @param msgId
     * @param message
     * @param user
     */
    public void handle(int msgId, GeneratedMessageV3 message, User user) {
        MsgReceiverFuntion func = receivers.get(msgId);
        if (func == null) {
            return;
        }

        func.accept(user, message);
    }
}
package com.lung.game.proto.msg;

@FunctionalInterface
public interface MsgReceiverFunction<T, R> {
    void accept(T t, R r);
}

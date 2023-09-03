package com.noseparte.game.bean.proto.msg;

@FunctionalInterface
public interface MsgReceiverFunction<T, R> {
    void accept(T t, R r);
}

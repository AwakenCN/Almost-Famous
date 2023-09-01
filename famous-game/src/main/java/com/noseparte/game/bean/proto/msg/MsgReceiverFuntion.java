package com.noseparte.game.bean.proto.msg;

@FunctionalInterface
public interface MsgReceiverFuntion<T, R> {
    void accept(T t, R r);
}

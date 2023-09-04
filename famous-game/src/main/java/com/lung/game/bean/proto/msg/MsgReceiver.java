package com.lung.game.bean.proto.msg;

import com.google.protobuf.Message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MsgReceiver {
    Class<? extends Message>[] value();
}
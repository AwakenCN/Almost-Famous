package com.lung.utils;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * @author noseparte
 * @implSpec
 * @since 2023/8/31 - 21:52
 * @version 1.0
 */
public class ChannelUtils {

    public static <T> void getAttr(Channel channel, AttributeKey<T> key) {
        if (null == channel) {
            return;
        }
        channel.attr(key).get();
    }

    public static <T> void setAttr(Channel channel, AttributeKey<T> key, T value) {
        if (null == channel) {
            return;
        }
        channel.attr(key).set(value);
    }

}

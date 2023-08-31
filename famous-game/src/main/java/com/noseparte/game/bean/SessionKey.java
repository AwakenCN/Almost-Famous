package com.noseparte.game.bean;

import io.netty.util.AttributeKey;

/**
 * @author noseparte
 * @implSpec
 * @since 2023/8/31 - 21:51
 * @version 1.0
 */
public class SessionKey {

    public static AttributeKey<String> TOKEN = AttributeKey.newInstance("token");


}

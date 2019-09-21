package com.noseparte.common.db.action;

import java.util.Date;

public abstract class GeneralAction<T> {

    //随机生成ID
    public static String getAutoId() {
        java.util.Random random = new java.util.Random();// 定义随机类
        int result = random.nextInt(1000);// 返回[0,10)集合中的整数，注意不包括10
        return String.valueOf(new Date().getTime()) + result;
    }

}

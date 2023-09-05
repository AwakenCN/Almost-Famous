package com.lung.utils;

import java.net.URL;

public class CommonUtils {

    /**
     * 得到class路径
     *
     * @return
     */
    public static String getClassPath() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource("//");
        return url.getPath().replace("%20", " ");
    }
}

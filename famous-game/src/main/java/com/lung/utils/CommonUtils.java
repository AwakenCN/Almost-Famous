package com.lung.utils;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static List<Integer> generateData(int start, int end) {
        return IntStream.range(start, end).boxed().collect(Collectors.toList());
    }
}

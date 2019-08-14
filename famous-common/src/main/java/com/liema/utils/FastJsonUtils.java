package com.liema.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Noseparte
 * @date 2019/8/13 15:08
 * @Description
 */
public class FastJsonUtils {

    public static JSONObject parseObject(String json) {
        return JSONObject.parseObject(json);
    }
}

package com.liema.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.liema.common.bean.BattleRankBean;

/**
 * @author Noseparte
 * @date 2019/8/13 15:08
 * @Description
 */
public class FastJsonUtils {

    public static JSONObject parseObject(String json) {
        return JSONObject.parseObject(json);
    }

    public static <T> T toBean(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz, Feature.OrderedField);
    }
}

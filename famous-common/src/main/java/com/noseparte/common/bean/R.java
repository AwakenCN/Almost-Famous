package com.noseparte.common.bean;


import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.KeyPrefix;

import java.util.HashMap;

/**
 * 返回数据
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error(ErrorCode code, String msg) {
        R r = new R();
        r.put("code", code.value());
        r.put("msg", msg);
        return r;
    }

    public static R ok() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public R responseBody(Object value) {
        super.put(KeyPrefix.RESPONSE, value);
        return this;
    }
}

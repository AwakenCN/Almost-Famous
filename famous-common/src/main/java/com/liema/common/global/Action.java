package com.liema.common.global;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.config.RedissonConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Noseparte
 * @date 2019/8/12 17:44
 * @Description
 */
@Data
public abstract class Action {

    @Autowired
    protected RedissonConfig redissonClient;

    protected int cmd;

    protected long rid;

    public abstract Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

}

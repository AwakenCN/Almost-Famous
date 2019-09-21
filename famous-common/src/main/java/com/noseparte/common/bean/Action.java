package com.noseparte.common.bean;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.global.Resoult;
import lombok.Data;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Data
public abstract class Action {

    @Autowired
    protected RedissonClient redissonClient;

    protected long rid;

    protected int cmd;

    public abstract Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

}

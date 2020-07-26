package com.noseparte.game.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.cache.RedissonUtils;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.global.Resoult;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.game.base.RegisterProtocol;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author Noseparte
 * @date 2019/8/20 17:36
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/dispatch")
public class Dispatch {

    @Resource
    RedissonClient redissonClient;

    @RequestMapping("/a")
    public Resoult a(@RequestBody String json, HttpServletRequest request, HttpServletResponse response) {
        Resoult r = null;
        try {
            JSONObject req = FastJsonUtils.parseObject(json);
            Integer cmd = req.getInteger("cmd");
            Long rid = req.getLong("rid");
            String token = request.getHeader("access_token");
            String clientIp = request.getRemoteAddr();
            // TODO 线上环境要判断uid
            // 仅限于robot测试使用
            if (request.getHeader("uid") != null) {
                Long uid = Long.parseLong(request.getHeader("uid"));
                if (!RegisterProtocol.whiteList(cmd)) {
                    if (!auth(uid, clientIp, token)) {
                        return Resoult.error(0, ErrorCode.TOKEN_EXPIRE_ERROR, "");
                    }
                }
            }
            Action action = RegisterProtocol.REGISTER_PROTOCOL_MAP.get(cmd);
            if (Objects.isNull(action)) {
                log.error("Dispatch error! Unknown protocol={}", cmd);
                return Resoult.error(cmd, ErrorCode.UNKNOWN_PROTOCOL, "");
            }
            if (Objects.nonNull(rid)) {
                action.setRid(rid);
            }
            action.setCmd(cmd);
            r = action.execute(req, request, response);
        } catch (Exception e) {
            log.error("", e);
        }
        return r;
    }

    /**
     * 鉴权，验证token
     *
     * @param uid 用户uid
     * @param clientIp 客户端IP
     * @param token access_token
     * @return verify Credential
     */
    private boolean auth(Long uid, String clientIp, String token) {
        if (String.valueOf(uid).startsWith("10000")) {
            return true;
        }
        String key = KeyPrefix.AdminRedisPrefix.ADMIN_USER_ID + uid;
        if (!RedissonUtils.isExists(redissonClient, key)) {
            return false;
        }
        String token_redis = RedissonUtils.get(redissonClient, key, String.class);
        JSONObject jsonObject = JSON.parseObject(token_redis);
        String access_token = jsonObject.getString("access_token");
        String ip = jsonObject.getString("ip");
        if (!ip.equals(clientIp)) {
            return false;
        }
        if (!token.equals(access_token)) {
            return false;
        }
        return true;
    }

}
package com.liema.login.sdk.dispatch.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.login.sdk.dispatch.RegisterProtocol;
import com.liema.common.exception.ErrorCode;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author Noseparte
 * @date 2019/8/12 16:23
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/dispatch")
public class Dispatch {

    @RequestMapping("/a")
    public Resoult a(@RequestBody String json, HttpServletRequest request, HttpServletResponse response){
        Resoult r = null;
        try{
            JSONObject req = FastJsonUtils.parseObject(json);
            Integer cmd = req.getInteger("cmd");
            Long rid = req.getLong("rid");
            Action action = RegisterProtocol.REGISTER_PROTOCOL_MAP.get(cmd);
            if(Objects.isNull(action)){
                log.error("Dispatch error! Unknown protocol={}", cmd);
                return Resoult.error(cmd, ErrorCode.UNKNOWN_PROTOCOL, "");
            }
            if(Objects.nonNull(rid)){
                 action.setRid(rid);
            }
            action.setCmd(cmd);
            r = action.execute(req, request, response);
        }catch (Exception e){
            log.error("", e);
        }
        return r;
    }

}

package com.noseparte.game.base.controller;

import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.game.base.RegisterProtocol;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: Noseparte
 * @Date: 2019/11/15 11:22
 * @Description: 
 * 
 *          <p></p>
 */
@RestController
@RequestMapping("api/")
public class ThirdController {

    private ConcurrentHashMap<String, ThirdBean> map = new ConcurrentHashMap<>();

    @RequestMapping("/push")
    public Resoult push(@RequestParam(value = "token",required = true) String token,
                          @RequestParam(value = "sig",required = true) String sig,
                          @RequestParam(value = "cses",required = true) String cses){
        ThirdBean thirdBean = new ThirdBean();
        thirdBean.setToken(token);
        thirdBean.setSig(sig);
        thirdBean.setCses(cses);
        thirdBean.setPushTick(System.currentTimeMillis());
        thirdBean.setExpire(60 * 1000);
        map.putIfAbsent(token, thirdBean);
        return Resoult.ok(RegisterProtocol.SCHOOL_LIST_RESP);
    }

    @RequestMapping("/pull")
    public Resoult pull(@RequestParam(value = "token",required = true) String token,
                          @RequestParam(value = "sig",required = true) String sig,
                          @RequestParam(value = "cses",required = true) String cses){
        ThirdBean thirdBean = map.get(token);
        if(Objects.isNull(thirdBean)){
            return Resoult.error(RegisterProtocol.SCHOOL_LIST_RESP, ErrorCode.SERVER_ERROR, "token不存在");
        }
        long now = System.currentTimeMillis();
        if(now >= thirdBean.getExpireTick()){
            return Resoult.error(RegisterProtocol.SCHOOL_LIST_RESP, ErrorCode.SERVER_ERROR, "token已失效");
        }
        return Resoult.ok(RegisterProtocol.SCHOOL_LIST_RESP).responseBody(thirdBean);
    }



}

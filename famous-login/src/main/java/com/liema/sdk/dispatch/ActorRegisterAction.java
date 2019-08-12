package com.liema.sdk.dispatch;

import com.alibaba.fastjson.JSONObject;
import com.liema.global.Action;
import com.liema.global.Resoult;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Noseparte
 * @date 2019/8/12 18:08
 * @Description
 */
@Component
public class ActorRegisterAction extends Action {

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}

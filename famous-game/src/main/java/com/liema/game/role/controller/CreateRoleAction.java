package com.liema.game.role.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Noseparte
 * @date 2019/8/20 17:50
 * @Description
 */
@Slf4j
@Component
public class CreateRoleAction extends Action {

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}

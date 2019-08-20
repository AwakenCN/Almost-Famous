package com.liema.game.base;

import com.liema.common.global.Action;
import com.liema.common.utils.SpringContextUtils;
import com.liema.game.role.controller.CreateRoleAction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/8/12 16:24
 * @Description
 */
public class RegisterProtocol {

    ////////// 玩家注册登录
    public final static int CREATE_ROLE_ACTION_REQ = 201;
    public final static int CREATE_ROLE_ACTION_RESP = 202;

    public static final Map<Integer, Action> REGISTER_PROTOCOL_MAP = new HashMap<Integer, Action>() {
        {
            //角色
            put(CREATE_ROLE_ACTION_REQ, SpringContextUtils.getBean("createRoleAction", CreateRoleAction.class));

        }
    };

}

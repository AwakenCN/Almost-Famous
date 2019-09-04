package com.liema.game.school.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.bean.CardGroup;
import com.liema.common.exception.ErrorCode;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.school.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AddOrUpdateCardGroupAction extends Action {

    @Autowired
    SchoolService schoolService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Integer schoolId = jsonObject.getInteger("schoolId");
        CardGroup cardGroup = jsonObject.getObject("cardGroup", CardGroup.class);
        // 新增或修改
        ErrorCode code = schoolService.updateCardGroup(rid, schoolId, cardGroup);
        return Resoult.error(RegisterProtocol.ADD_OR_UPDATE_CARD_GROUP_RESP, code, "");
    }
}

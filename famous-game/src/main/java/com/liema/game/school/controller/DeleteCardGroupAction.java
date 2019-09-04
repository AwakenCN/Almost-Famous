package com.liema.game.school.controller;

import com.alibaba.fastjson.JSONObject;
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
public class DeleteCardGroupAction extends Action {

    @Autowired
    SchoolService schoolService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Integer schoolId = jsonObject.getInteger("schoolId");
        Long groupId = jsonObject.getLong("groupId");
        ErrorCode code = schoolService.deleteCardGroup(rid, schoolId, groupId);
        return Resoult.error(RegisterProtocol.SCHOOL_DELETE_CARD_GROUP_RESP, code, "");
    }
}

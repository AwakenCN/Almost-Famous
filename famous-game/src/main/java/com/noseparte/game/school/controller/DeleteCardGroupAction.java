package com.noseparte.game.school.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.school.service.SchoolService;
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

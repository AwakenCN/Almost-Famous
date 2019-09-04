package com.liema.game.school.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.school.entity.School;
import com.liema.game.school.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class SchoolListAction extends Action {

    @Autowired
    SchoolService schoolService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        School school = schoolService.getSchoolByRoleId(rid);
        return Resoult.ok(RegisterProtocol.SCHOOL_LIST_RESP).responseBody(school);
    }
}

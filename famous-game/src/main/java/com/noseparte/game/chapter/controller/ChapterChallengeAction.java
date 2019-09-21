package com.noseparte.game.chapter.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.chapter.service.ChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class ChapterChallengeAction extends Action {

    @Resource
    ChapterService chapterService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Integer chapterId = jsonObject.getInteger("chapterId");
        Integer schoolId = jsonObject.getInteger("schoolId");
        ErrorCode errorCode = chapterService.challenge(rid, schoolId, chapterId);
        if (errorCode != ErrorCode.SERVER_SUCCESS) {
            return Resoult.error(RegisterProtocol.CHAPTER_CHALLENGE_RESP, errorCode, "");
        }
        return Resoult.ok(RegisterProtocol.CHAPTER_CHALLENGE_RESP);
    }
}

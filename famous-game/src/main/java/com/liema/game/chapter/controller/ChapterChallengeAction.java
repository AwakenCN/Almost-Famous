package com.liema.game.chapter.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.exception.ErrorCode;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.chapter.service.ChapterService;
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

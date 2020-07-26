package com.noseparte.game.chapter.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.global.Resoult;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.chapter.entity.Chapter;
import com.noseparte.game.chapter.service.ChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/15 16:46
 * @Description: 
 * 
 *          <p>玩家关卡进度</p>
 */
@Slf4j
@Component
public class ChapterProgressAction extends Action {

    @Resource
    private ChapterService chapterService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Chapter chapter = chapterService.getChapter(rid);
        if(log.isInfoEnabled()){
            log.info("玩家的关卡进度列表======== rid={}, chapters={}", rid, chapter.getChapters().keySet());
        }
        return Resoult.ok(RegisterProtocol.CHAPTER_PROGRESS_RESP).responseBody(chapter);
    }

}

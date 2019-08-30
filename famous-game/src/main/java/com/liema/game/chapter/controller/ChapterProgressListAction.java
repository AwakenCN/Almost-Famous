package com.liema.game.chapter.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.chapter.service.ChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ChapterProgressListAction extends Action {

    @Resource
    ChapterService chapterService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        List<Integer> chapterIds = chapterService.progressList(rid);
        if (null == chapterIds) {
            return Resoult.ok(RegisterProtocol.CHAPTER_PROGRESS_LIST_RESP);
        }

        chapterIds.sort(Integer::compareTo);
        Map<String, Object> data = new HashMap<>();
        data.put("rid", rid);
        data.put("chapterIds", chapterIds);

        return Resoult.ok(RegisterProtocol.CHAPTER_PROGRESS_LIST_RESP).responseBody(data);
    }
}

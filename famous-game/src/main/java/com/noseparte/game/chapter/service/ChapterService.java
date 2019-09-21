package com.noseparte.game.chapter.service;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.exception.ErrorCode;

import java.util.List;

public interface ChapterService {

    ErrorCode challenge(Long rid, Integer schooldId, Integer chapterId);

    List<Integer> progressList(Long rid);

    JSONObject challengeOver(Long rid, Integer schoolId, Integer chapterId, Integer state);

    boolean hasPass(Long rid, Integer subType, Integer chapterId);

    void initChapter(long rid);

    int getActorChapterProgressMax(Long rid);
}

package com.liema.game.chapter.service;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.exception.ErrorCode;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liang
 * @since 2019-06-18
 */
public interface ChapterService {

    ErrorCode challenge(Long rid, Integer schooldId, Integer chapterId);

    List<Integer> progressList(Long rid);

    JSONObject challengeOver(Long rid, Integer schoolId, Integer chapterId, Integer state);

    boolean hasPass(Long rid, Integer subType, Integer chapterId);

    void initChapter(long rid);

    int getActorChapterProgressMax(Long rid);
}

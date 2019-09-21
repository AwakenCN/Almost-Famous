package com.noseparte.game.chapter.mongo;

import com.noseparte.common.db.dao.GeneralDao;
import com.noseparte.game.chapter.entity.Chapter;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author liang
 * @since 2019-06-18
 */
@Repository
public interface ChapterDao extends GeneralDao<Chapter> {

    void addChapter(Chapter chapter);

    void updateChapter(Chapter chapter);

    Chapter getChapterById(Long rid);

}

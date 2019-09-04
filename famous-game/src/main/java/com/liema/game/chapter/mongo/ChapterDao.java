package com.liema.game.chapter.mongo;

import com.liema.game.chapter.entity.Chapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author liang
 * @since 2019-06-18
 */
@Repository
public class ChapterDao {


    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;

    public void addChapter(Chapter chapter){

    }


    public void updateChapter(Chapter chapter){

    }

    public Chapter getChapterById(Long rid){
        return null;
    }

}

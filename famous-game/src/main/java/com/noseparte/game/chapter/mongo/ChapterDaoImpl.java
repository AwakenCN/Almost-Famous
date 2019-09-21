package com.noseparte.game.chapter.mongo;

import com.noseparte.common.db.dao.GeneralDaoImpl;
import com.noseparte.game.chapter.entity.Chapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Noseparte
 * @date 2019/9/10 11:48
 * @Description
 */
@Repository
public class ChapterDaoImpl extends GeneralDaoImpl<Chapter> implements ChapterDao {

    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;

    @Override
    protected Class<Chapter> getEntityClass() {
        return Chapter.class;
    }

    @Override
    public void addChapter(Chapter chapter) {
        gameMongoTemplate.insert(chapter);
    }

    @Override
    public void updateChapter(Chapter chapter) {
        Query query = new Query(Criteria.where("rid").is(chapter.getRid()));
        Update update = new Update().set("chapters", chapter.getChapters());
        gameMongoTemplate.findAndModify(query, update, getEntityClass());
    }

    @Override
    public Chapter getChapterById(Long rid) {
        return gameMongoTemplate.findOne(new Query(Criteria.where("rid").is(rid)), getEntityClass());
    }
}

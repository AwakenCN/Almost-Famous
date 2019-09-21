package com.noseparte.game.school.mongo;

import com.noseparte.common.db.dao.GeneralDaoImpl;
import com.noseparte.game.school.entity.School;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Noseparte
 * @date 2019/9/10 10:46
 * @Description
 */
@Repository
public class SchoolDaoImpl extends GeneralDaoImpl<School> implements SchoolDao {

    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;

    @Override
    protected Class<School> getEntityClass() {
        return School.class;
    }

    public boolean addSchool(School school) {
        gameMongoTemplate.insert(school);
        return true;
    }

    public boolean upSchool(School school) {
        Query query = new Query().addCriteria(Criteria.where("rid").is(school.getRid()));
        Update update = new Update()
                .set("schools", school.getSchools());
        gameMongoTemplate.findAndModify(query, update, getEntityClass());
        return true;
    }

    public School getSchool(long rid) {
        return gameMongoTemplate.findOne(new Query(Criteria.where("rid").is(rid)), getEntityClass());
    }


}

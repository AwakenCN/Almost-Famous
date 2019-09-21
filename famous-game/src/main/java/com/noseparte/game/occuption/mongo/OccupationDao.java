package com.noseparte.game.occuption.mongo;

import com.noseparte.game.occuption.entity.Occupation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class OccupationDao {

    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;


    public void insert(Occupation occupation) {
        gameMongoTemplate.insert(occupation);
    }

    public List<Occupation> getOccupationalGroup(int schoolId) {
        return gameMongoTemplate.find(new Query(Criteria.where("schoolId").is(schoolId)), Occupation.class);
    }
}

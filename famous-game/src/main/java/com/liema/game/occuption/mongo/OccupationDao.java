package com.liema.game.occuption.mongo;

import com.liema.game.occuption.entity.Occupation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class OccupationDao {

    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;


    public void insert(Occupation target) {

    }

    public List<Occupation> getOccupationalGroup(int schoolId) {
        return null;
    }
}

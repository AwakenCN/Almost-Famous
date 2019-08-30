package com.liema.game.school.mongo;

import com.liema.game.school.entity.School;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;

/**
 * <p>
 * 职业 Mapper 接口
 * </p>
 *
 * @author liang
 * @since 2019-04-18
 */
public class SchoolDao {

    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;


    public static boolean addSchool(School school) {

        return false;
    }

    public static boolean upSchool(School school) {
        return false;
    }

    public static School getSchool(long rid) {
        return null;
    }
}

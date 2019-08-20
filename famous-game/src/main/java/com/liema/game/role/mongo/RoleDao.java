package com.liema.game.role.mongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Noseparte
 * @date 2019/8/20 18:14
 * @Description
 */
@Slf4j
@Repository
public class RoleDao {

    @Resource
    @Qualifier(value = "gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;




}

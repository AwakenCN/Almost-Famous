package com.liema.sdk.internal.mongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author Noseparte
 * @date 2019/8/12 18:10
 * @Description
 */
@Slf4j
public class AccountMongoTemplate {

    @Autowired
    @Qualifier("account")
    private MongoTemplate accountMongoTemplate;



}

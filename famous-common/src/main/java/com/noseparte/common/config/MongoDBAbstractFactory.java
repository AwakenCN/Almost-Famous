package com.noseparte.common.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * @author Noseparte
 * @date 2019/8/12 17:05
 * @Description
 */
@Slf4j
@Data
public abstract class MongoDBAbstractFactory {

    private String host, database, username, password;
    private int port;

    public MongoDbFactory mongoDbFactory() throws Exception {
        ServerAddress address = new ServerAddress(host, port);
        // 采用建造者模式才配置MongoDB初始化的参数
        MongoClientOptions options = MongoClientOptions.builder().connectTimeout(30000).build();
        MongoClient mongoClient = null;
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());
            mongoClient = new MongoClient(address, credential, options);
        } else {
            mongoClient = new MongoClient(address);
        }
        SimpleMongoDbFactory factory = new SimpleMongoDbFactory(mongoClient, database);
        return factory;
    }

    abstract public MongoTemplate getMongoTemplate() throws Exception;


}

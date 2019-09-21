package com.noseparte.game.base.config;

import com.noseparte.common.config.MongoDBAbstractFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Noseparte
 * @date 2019/8/13 17:32
 * @Description
 */
@Configuration
@Component
@ConfigurationProperties(prefix = "spring.data.mongodb.game")
public class GameCoreMongonConfig extends MongoDBAbstractFactory {

    @Override
    @Bean(name = "gameMongoTemplate")
    public MongoTemplate getMongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}

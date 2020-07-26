package com.noseparte.login.sdk.internal.config;

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
@ConfigurationProperties(prefix = "spring.data.mongodb.login")
public class AccountMongonConfig extends MongoDBAbstractFactory {

    @Override
    @Bean(name = "accountMongoTemplate")
    public MongoTemplate getMongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}

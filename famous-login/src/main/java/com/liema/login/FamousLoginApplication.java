package com.liema.login;

import com.liema.login.listener.ApplicationEventListener;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @author Noseparte
 * @date 2019/7/31 10:43
 * @Description
 */
@ImportAutoConfiguration(RedissonAutoConfiguration.class)
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class FamousLoginApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FamousLoginApplication.class);
        application.addListeners(new ApplicationEventListener());
        application.run(args);
    }

}

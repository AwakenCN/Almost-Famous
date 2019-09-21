package com.noseparte.login;

import com.noseparte.login.listener.ApplicationEventListener;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Noseparte
 * @date 2019/7/31 10:43
 * @Description
 */
@ImportAutoConfiguration(RedissonAutoConfiguration.class)
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ComponentScan({"com.noseparte.login.*", "com.noseparte.common.*"})
public class FamousLoginApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FamousLoginApplication.class);
        application.addListeners(new ApplicationEventListener());
        application.run(args);
    }

}

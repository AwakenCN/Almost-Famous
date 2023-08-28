package com.noseparte.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @Auther: Noseparte
 * @Date: 2019/9/27 17:19
 * @Description:
 *
 *          <p>Famous-Game服务 启动类</p>
 *          <p>注册到Eureka服务中心</p>
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class FamousGameApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FamousGameApplication.class);
        application.run(args);
    }


}

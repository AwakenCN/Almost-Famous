package com.noseparte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Auther: Noseparte
 * @Date: 2019/9/27 16:58
 * @Description:
 *
 *          <p>Spring Cloud Eureka Server 注册中心</p>
 */

@EnableEurekaServer
@SpringBootApplication
public class FamousCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamousCloudApplication.class, args);
    }

}

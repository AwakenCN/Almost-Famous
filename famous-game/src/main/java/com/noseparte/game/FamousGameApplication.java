package com.noseparte.game;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.noseparte.game.base.listener.ApplicationEventListener;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Noseparte
 * @Date: 2019/9/27 17:19
 * @Description: 
 * 
 *          <p>Famous-Game服务 启动类</p>
 *          <p>注册到Eureka服务中心</p>
 */
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker
@ImportAutoConfiguration(RedissonAutoConfiguration.class)
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ComponentScan({"com.noseparte.game.*", "com.noseparte.common.*"})
@RestController
public class FamousGameApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FamousGameApplication.class);
        application.addListeners(new ApplicationEventListener());
        application.run(args);
    }

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    @HystrixCommand(fallbackMethod = "hiError")
    public String home(@RequestParam String name) {
        return "hi " + name + ",i am from port:" + port;
    }

    public String hiError(String name) {
        return "hi," + name + ",sorry,error!";
    }


}

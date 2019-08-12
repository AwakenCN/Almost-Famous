package com.liema;

import com.liema.listener.ApplicationEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Noseparte
 * @date 2019/7/31 10:43
 * @Description
 */
@SpringBootApplication
public class FamousLoginApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FamousLoginApplication.class);
        application.addListeners(new ApplicationEventListener());
        application.run(args);
    }

}

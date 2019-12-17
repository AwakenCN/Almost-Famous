package com.noseparte.robot;

import com.noseparte.common.thread.ThreadPool;
import com.noseparte.robot.login.LoginCmd;
import com.noseparte.robot.login.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/11 11:57
 * @Description: <p>Robot 服务总线</p>
 * <p>注册登录Robot 执行调度任务</p>
 */
@Slf4j
@ComponentScan({"com.noseparte.robot", "com.noseparte.common.*"})
@SpringBootApplication
public class FamousRobotApplication implements CommandLineRunner {

    @Resource
    RobotConfig robotConfig;

    public static ThreadPool pool;

    public static int threadCount;
    public static String adminUrl;
    public static String gameCoreUrl;
    public static int robotType, robotCount, index;
    public static String accountPre;
    public static String robotModel;

    public static void main(String[] args) {
        String configPath;
        if (args.length > 0) {
            configPath = args[0] + "/gamedata";
        } else {
            configPath = FamousRobotApplication.class.getResource("/gamedata").getFile();
        }
        SpringApplication application = new SpringApplication(FamousRobotApplication.class);
        application.addListeners(new ApplicationEventListener(configPath));
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    public static void login(int index, int robotCount, String accountPre) {

        for (int i = index; i < robotCount + index; i++) {
            LoginCmd loginCmd = new LoginCmd();
            loginCmd.setCmd(RegisterProtocol.USER_LOGIN_ACTION_REQ);
            loginCmd.setPassword("123456");
            loginCmd.setAccount(accountPre + i);
            loginCmd.setIndex(i);
            try {
                new LoginRequest(loginCmd).execute();
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    @Override
    public void run(String... args) {
        threadCount = robotConfig.getRobotCount();
        adminUrl = robotConfig.getAdminUrl();
        gameCoreUrl = robotConfig.getGameCoreUrl();
        robotCount = robotConfig.getRobotCount();
        robotType = robotConfig.getRobotType();
        index = robotConfig.getRobotIndex();
        accountPre = robotConfig.getAccountPre();
        robotModel = robotConfig.getRobotModel();
        // init thread pool
        pool = new ThreadPool(threadCount, "机器人网关线程池");
        // login
        login(index, robotCount, accountPre);

        // print login count
        new Timer("", true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int loginCount = RobotMgr.getInstance().getLoginCount();
                log.error("登录人数={}", loginCount);
            }
        }, 5000, 10000);
    }

}

package com.noseparte.battle;

import LockstepProto.NetMessage;
import com.noseparte.battle.battle.*;
import com.noseparte.battle.match.*;
import com.noseparte.battle.server.BattleServer;
import com.noseparte.battle.server.CHeartBeat;
import com.noseparte.battle.server.SHeartBeat;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.rpc.RpcClient;
import com.noseparte.common.thread.ThreadPool;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/7/31 9:58
 * @Description
 *
 *      <title>战斗服 启动</title>
 *      <p>启动匹配线程</p>
 *      <p>matchMgr.start();</p>
 *      <p>启动网络放到最后</p>
 *      <p>nettyServer.start();</p>
 */
@EnableEurekaClient
@SpringBootApplication
@ComponentScan({"com.noseparte.battle", "com.noseparte.common.*"})
public class FamousBattleApplication implements CommandLineRunner {

    @Resource
    BattleServer nettyServer;
    @Resource
    MatchMgr matchMgr;
    @Resource
    BattleServerConfig battleServerConfig;
    @Resource
    RpcClient rpcClient;

    public static ThreadPool pool;

    public static Map<Integer, Class> protocolMap = new HashMap<Integer, Class>() {
        {
            put(NetMessage.C2S_HeartBeat_VALUE, CHeartBeat.class);
            put(NetMessage.S2C_HeartBeat_VALUE, SHeartBeat.class);
            put(NetMessage.C2S_Match_VALUE, CMatch.class);
            put(NetMessage.S2C_Match_VALUE, SMatch.class);
            put(NetMessage.C2S_State_VALUE, CState.class);
            put(NetMessage.S2C_State_VALUE, SState.class);
            put(NetMessage.C2S_LockStep_VALUE, CLockStep.class);
            put(NetMessage.S2C_LockStep_VALUE, SLockStep.class);
            put(NetMessage.C2S_Reconnect_VALUE, CReconnect.class);
            put(NetMessage.S2C_Reconnect_VALUE, SReconnect.class);
            put(NetMessage.C2S_PullFrame_VALUE, CPullFrame.class);
            put(NetMessage.S2C_PullFrame_VALUE, SPullFrame.class);
            put(NetMessage.C2S_BattleEnd_VALUE, CBattleEnd.class);
            put(NetMessage.S2C_BattleEnd_VALUE, SPullFrame.class);
            put(NetMessage.C2S_MatchCancel_VALUE, CMatchCancel.class);
            put(NetMessage.S2C_MatchCancel_VALUE, SMatchCancel.class);
        }
    };

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FamousBattleApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @PreDestroy
    public void stop() {
        nettyServer.close();
        matchMgr.close();
        pool.shutdown();
        rpcClient.close();
    }

    @Override
    public void run(String... args) throws Exception {
        String configPath;
        if (args.length > 0) {
            configPath = args[0] + "/gamedata";
        } else {
            configPath = FamousBattleApplication.class.getResource("/gamedata").getFile();
        }
        // load策划数据
        ConfigManager.loadGameData(configPath);
        // init thread pool
        pool = new ThreadPool(battleServerConfig.getThreadCount());
        // 启动匹配线程
        matchMgr.start();
        // 启动网络放到最后
        nettyServer.start();
    }

}

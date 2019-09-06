package com.liema.battle;

/**
 * @author Noseparte
 * @date 2019/7/31 9:58
 * @Description
 */

import LockstepProto.NetMessage;
import com.liema.battle.battle.*;
import com.liema.battle.match.*;
import com.liema.battle.server.BattleServer;
import com.liema.battle.server.CHeartBeat;
import com.liema.battle.server.SHeartBeat;
import com.liema.common.global.ConfigManager;
import com.liema.common.rpc.RpcClient;
import com.liema.common.thread.ThreadPool;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/7/31 9:58
 * @Description
 */
@SpringBootApplication
@ComponentScan({"com.liema.battle", "com.liema.common.*"})
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
//        application.addListeners(new SchedulerConfig());
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

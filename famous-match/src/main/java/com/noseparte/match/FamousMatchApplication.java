package com.noseparte.match;

import LockstepProto.NetMessage;
import com.noseparte.common.battle.BattleService;
import com.noseparte.common.battle.server.CHeartBeat;
import com.noseparte.common.battle.server.LinkMgr;
import com.noseparte.common.battle.server.RegistryProtocol;
import com.noseparte.common.battle.server.SHeartBeat;
import com.noseparte.common.cache.RedissonUtils;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.rpc.RpcClient;
import com.noseparte.common.thread.ThreadPool;
import com.noseparte.match.match.*;
import com.noseparte.match.server.MatchServer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@EnableEurekaClient
@SpringBootApplication
@ComponentScan({"com.noseparte.match", "com.noseparte.common.*"})
public class FamousMatchApplication implements CommandLineRunner {

    @Resource
    MatchServer nettyServer;
    @Resource
    RankMatchMgr rankMatchMgr;
    @Resource
    LeisureMatchMgr leisureMatchMgr;
    @Resource
    MatchServerConfig matchServerConfig;
    @Resource
    RpcClient rpcClient;
    @Resource
    RedissonClient redissonClient;

    public static ThreadPool pool;

    Timer pullBattleServicesTimer = new Timer(true);

    public static ConcurrentSkipListSet<BattleService> battleServices = new ConcurrentSkipListSet<>();

    static {
        RegistryProtocol.protocolMap.put(NetMessage.C2S_HeartBeat_VALUE, CHeartBeat.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_HeartBeat_VALUE, SHeartBeat.class);
        RegistryProtocol.protocolMap.put(NetMessage.C2S_Match_VALUE, CMatch.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_Match_VALUE, SMatch.class);
        RegistryProtocol.protocolMap.put(NetMessage.C2S_Reconnect_VALUE, CReconnect.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_Reconnect_VALUE, SReconnect.class);
        RegistryProtocol.protocolMap.put(NetMessage.C2S_MatchCancel_VALUE, CMatchCancel.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_MatchCancel_VALUE, SMatchCancel.class);
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FamousMatchApplication.class);
//        application.addListeners(new SchedulerConfig());
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @PreDestroy
    public void stop() {
        nettyServer.close();
        rankMatchMgr.close();
        leisureMatchMgr.close();
        pool.shutdown();
        rpcClient.close();
        pullBattleServicesTimer.cancel();
    }

    @Override
    public void run(String... args) throws Exception {
        String configPath;
        if (args.length > 0) {
            configPath = args[0] + "/gamedata";
        } else {
            configPath = FamousMatchApplication.class.getResource("/gamedata").getFile();
        }
        // load策划数据
        ConfigManager.loadGameData(configPath);
        // init thread pool
        pool = new ThreadPool(matchServerConfig.getThreadCount(), "Match服网关游戏逻辑协议线程池");
        // 拉取战斗服务器列表
        pullBattleServicesTimer.scheduleAtFixedRate(new PullBattleServices(), 0, 5000);
        // 启动匹配线程
        rankMatchMgr.start();
        LinkMgr.addObserver(rankMatchMgr);// 注册连接关闭观察者
        leisureMatchMgr.start();
        LinkMgr.addObserver(leisureMatchMgr);
        // 启动网络放到最后
        nettyServer.start();
    }

    class PullBattleServices extends TimerTask {
        @Override
        public void run() {
            String battle_server_service_list_key = KeyPrefix.BattleRedisPrefix.BATTLE_SERVER_SERVICE_LIST;
            List<Integer> serverIds = redissonClient.getList(battle_server_service_list_key);
            if (serverIds == null || serverIds.size() == 0) {
                log.error("No battle server available.");
                return;
            }
            List<Integer> deleteServerIds = new ArrayList<>();
            for (Integer serverId : serverIds) {
                String battle_server_service_key = KeyPrefix.BattleRedisPrefix.BATTLE_SERVER_SERVICE + serverId;
                if (!RedissonUtils.lock(redissonClient, battle_server_service_key)) {
                    continue;
                }
                BattleService battleService = RedissonUtils.get(redissonClient, battle_server_service_key, BattleService.class);
                RedissonUtils.unlock(redissonClient, battle_server_service_key);
                if (null == battleService) {
                    deleteServerIds.add(serverId);
                    continue;
                }
                if (null != battleService) {
                    battleServices.clear();
                    battleServices.add(battleService);
                }
            }
            // 删除没有服务器信息的战斗服务器
            for (Integer deleteServerId : deleteServerIds) {
                if (!RedissonUtils.lock(redissonClient, battle_server_service_list_key)) {
                    continue;
                }
                RedissonUtils.delete_list(deleteServerId, redissonClient, battle_server_service_list_key);
                RedissonUtils.unlock(redissonClient, battle_server_service_list_key);
            }
        }
    }


    // 选择战斗服务器
    public BattleService selectBattleService() {
        return this.battleServices.first();
    }



}

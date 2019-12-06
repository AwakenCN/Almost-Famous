package com.noseparte.match.match;

import com.noseparte.common.battle.BattleModeEnum;
import com.noseparte.common.battle.MatchEnum;
import com.noseparte.common.battle.SimpleActor;
import com.noseparte.common.bean.Actor;
import com.noseparte.common.bean.BattleRankBean;
import com.noseparte.common.cache.RedissonUtils;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.resources.BattleRankConf;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSortedSet;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class RankMatchMgr extends AbstractMatch {

    Object nextRunnableLock = new Object();
    @Setter
    private volatile boolean stop = false;

    @Override
    public void add(Actor actor) {
        BattleRankBean battleRankBean = actor.getBattleRankBean();
        int rankId = battleRankBean.getRankId();
        int startCount = battleRankBean.getStarCount();
        int totalStarCount = this.getTotalStarCount(rankId, startCount);
        String match_queue_key = KeyPrefix.BattleRedisPrefix.MATCH_RANK_MODE_QUEUE + generatorKey(rankId);
        try {
            String match_queue_by_role_key = KeyPrefix.BattleRedisPrefix.MATCH_QUEUE_BY_ROLE + actor.getRid();
            boolean isExits = RedissonUtils.isExists(redissonClient, match_queue_by_role_key);
            if (isExits) {
                log.error("已存在{}，不能重复匹配.", match_queue_by_role_key);
                return;
            }
            RedissonUtils.lock(redissonClient, match_queue_key);
            RedissonUtils.set(actor, redissonClient, match_queue_by_role_key, matchServerConfig.getMatchMaxWaitTime());
            RSortedSet<SimpleActor> sortedSet = redissonClient.getSortedSet(match_queue_key);
            if (sortedSet.size() > 0) {
                SimpleActor simpleActor = sortedSet.first();
                if (simpleActor.getRoleId() == actor.getRid())
                    return;
                sortedSet.remove(simpleActor);
                createSimpleBattleRoom(BattleModeEnum.RANK, simpleActor.getRoleId(), actor.getRid());
                return;
            }
            SimpleActor simpleActor = new SimpleActor(actor.getRid(), actor.getSchoolLevel(), actor.getMatchBeginTime());
            sortedSet.add(simpleActor);
        } catch (Exception e) {
            log.error("add rank match error: ", e);
        } finally {
            RedissonUtils.unlock(redissonClient, match_queue_key);
        }
    }

    @Override
    public void remove(Actor actor, MatchEnum reason) {
        if (actor.getBattleMode() != BattleModeEnum.RANK)
            return;
        String match_queue_key = KeyPrefix.BattleRedisPrefix.MATCH_RANK_MODE_QUEUE + this.generatorKey(actor.getBattleRankBean().getRankId());
        try {
            RedissonUtils.lock(redissonClient, match_queue_key);
            RSortedSet<SimpleActor> sortedSet = redissonClient.getSortedSet(match_queue_key);
            SimpleActor simpleActor = new SimpleActor(actor.getRid(), actor.getSchoolLevel(), actor.getMatchBeginTime());
            sortedSet.remove(simpleActor);
            if (log.isDebugEnabled()) {
                log.debug("从段位模式匹配队列中移出玩家role={}, reason={}", actor.getRid(), reason);
            }
        } finally {
            RedissonUtils.unlock(redissonClient, match_queue_key);
        }
    }

    @Override
    public void run() {
        List<String> matchQueueAllKeys = getMatchQueueAllKeys();
        synchronized (nextRunnableLock) {
            while (stop) {
                try {
                    for (String key : matchQueueAllKeys) {
                        try {
                            if (!RedissonUtils.lock(redissonClient, key)) {
                                continue;
                            }
                            RSortedSet<SimpleActor> sortedSet = redissonClient.getSortedSet(key);
                            if (sortedSet.size() == 0) {
                                continue;
                            }
                            long now = System.currentTimeMillis();
                            Collection<SimpleActor> simpleActors = sortedSet.readAll();
                            simpleActors.stream().forEach(simpleActor -> {
                                long matchBeginTime = simpleActor.getMatchBeginTime();
                                long remainingTime = (now - matchBeginTime) / 1000;
                                if (remainingTime > matchServerConfig.getMatchMaxWaitTime()) {
                                    ////////////////////////////////
                                    // Do something overtime
                                    sortedSet.remove(simpleActor);
                                }
                            });
                        } finally {
                            RedissonUtils.unlock(redissonClient, key);
                        }
                    }
                    nextRunnableLock.wait(3000);
                } catch (Exception e) {
                    log.error("段位匹配出错{}", e);
                }
            }
        }
    }

    @Override
    public void start() {
        synchronized (nextRunnableLock) {
            if (this.stop) {
                log.error("重复启动段位模式匹配战斗线程");
                return;
            }
            setStop(true);
            Thread t = new Thread(this);
            t.setDaemon(true);
            t.setName("RankMatchMgr");
            t.start();
            log.error("段位模式匹配战斗线程启动");
        }
    }

    @Override
    public void close() {
        synchronized (nextRunnableLock) {
            if (!this.stop) {
                log.error("已经停止段位模式匹配战斗线程.");
                return;
            }
            setStop(false);
            log.error("停止段位模式匹配战斗线程....");
        }
    }

    /**
     * @param rankId    段位id
     * @param starCount 星数
     * @return 返回角色当前段位总星数
     */
    public int getTotalStarCount(int rankId, int starCount) {
        BattleRankConf battleRankConf = ConfigManager.battleRankConfMap.get(rankId);
        return battleRankConf.getMinstar() + starCount;
    }

    /**
     * 段位key
     *
     * @param rankId
     * @return
     */
    public String generatorKey(int rankId) {
        BattleRankConf battleRankConf = ConfigManager.battleRankConfMap.get(rankId);
        return battleRankConf.getMinstar() + "_" + battleRankConf.getMaxstar();
    }

    /**
     * @return 段位配表中所有的段位keys
     */
    private List<String> getMatchQueueAllKeys() {
        List<String> keys = new ArrayList<>();
        Set<Map.Entry<Integer, BattleRankConf>> entries = ConfigManager.battleRankConfMap.entrySet();
        Iterator<Map.Entry<Integer, BattleRankConf>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, BattleRankConf> entry = iterator.next();
            String match_queue_key = KeyPrefix.BattleRedisPrefix.MATCH_RANK_MODE_QUEUE + this.generatorKey(entry.getKey());
            keys.add(match_queue_key);
        }
        return keys;
    }

}

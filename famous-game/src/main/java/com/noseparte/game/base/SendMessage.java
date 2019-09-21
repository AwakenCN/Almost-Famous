package com.noseparte.game.base;

import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.global.Resoult;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SendMessage {

    @Autowired
    protected RedissonClient redissonClient;

    public void send(List<Long> rids, Resoult resoult) {
        for (Long rid : rids) {
            send(rid, resoult);
        }

    }

    public void send(long rid, Resoult resoult) {
        String key = KeyPrefix.GameCoreRedisPrefix.ROLE_MESSAGE_QUEUE + rid;
//        RLock lock = redissonClient.getFairLock(key);
        try {
//            boolean isLock = lock.tryLock(300, 500, TimeUnit.MILLISECONDS);
//            if (isLock) {
            RBlockingQueue<Resoult> queue = redissonClient.getBlockingQueue(key);
            if (null == queue) {
                return;
            }
            queue.offer(resoult);
//            }
        } catch (Exception e) {
            log.error("SendMessage:", e);
        } finally {
//            lock.unlock();
        }
    }

    /**
     * 更新队列
     *
     * @param rid
     * @return
     */
    public Map<String, Object> pollUpdateMsg(long rid) {
        Map<String, Object> data = new HashMap<>();
        List<Resoult> resoults = new ArrayList<>();
        String key = KeyPrefix.GameCoreRedisPrefix.ROLE_MESSAGE_QUEUE + rid;
//        RLock lock = redissonClient.getFairLock(key);
        try {
            //           boolean isLock = lock.tryLock(300, 500, TimeUnit.MILLISECONDS);
            //           if (isLock) {
            RBlockingQueue<Resoult> queue = redissonClient.getBlockingQueue(key);
            int size = queue.size();
            for (int i = size; i > 0; i--) {
                Resoult res = queue.poll();
                if (null == res)
                    continue;
                resoults.add(res);
            }
            data.put("updates", resoults);
            //          }
        } catch (Exception e) {
            log.error("Hearbeat:", e);
        } finally {
            //     lock.unlock();
        }

        return data;
    }

    public Resoult sendNow(long rid) {
        Map<String, Object> data = pollUpdateMsg(rid);
        return Resoult.ok(RegisterProtocol.HEART_BEAT_ACTION_RESP).responseBody(data);
    }

}

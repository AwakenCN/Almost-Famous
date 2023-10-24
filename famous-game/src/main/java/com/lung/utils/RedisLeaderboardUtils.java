package com.lung.utils;

import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

public class RedisLeaderboardUtils {
    public static void main(String[] args) {
        // 创建 Redisson 客户端连接
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        RedissonClient redissonClient = Redisson.create(config);

        // 添加成员到排行榜
//        addMemberToLeaderboard(redissonClient, "member1", 100);
//        addMemberToLeaderboard(redissonClient, "member2", 150);
//        addMemberToLeaderboard(redissonClient, "member3", 100);

        addMemberToLeaderboard(redissonClient, "member4", 300);

        // 获取排行榜上的成员
        RScoredSortedSet<String> leaderboard = getLeaderboard(redissonClient);

        // 输出排行榜上的成员和分数
        for (String member : leaderboard) {
            System.out.println("Member: " + member + ", Score: " + leaderboard.getScore(member));
        }

        // 关闭 Redisson 客户端
        redissonClient.shutdown();
    }

    // 将成员添加到排行榜
    private static void addMemberToLeaderboard(RedissonClient redissonClient, String member, double score) {
        // 获取当前时间的秒数
        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        // 获取 Redisson 的 RScoredSortedSet 对象
        RScoredSortedSet<String> leaderboard = redissonClient.getScoredSortedSet("leaderboard", StringCodec.INSTANCE);

        // 组合分数和当前时间的小数作为 score，添加成员到排行榜
        leaderboard.add(score + currentTimeSeconds / 1000.0, member);
    }

    // 获取排行榜上的成员
    private static RScoredSortedSet<String> getLeaderboard(RedissonClient redissonClient) {
        // 获取 Redisson 的 RScoredSortedSet 对象
        return redissonClient.getScoredSortedSet("leaderboard", StringCodec.INSTANCE);
    }
}

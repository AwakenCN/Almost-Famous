package com.liema.common.global;

/**
 * @author Noseparte
 * @date 2019/8/8 12:46
 * @Description
 */
public class KeyPrefix {

    /************************Response***********************/
    public static final String RESPONSE = "data";

    /************************Redis***********************/
    public static class AdminRedisPrefix {
        /*token*/
        public static final String ADMIN_USER_ID = "admin_user_id:";
    }

    public static class GameCoreRedisPrefix {
        /*role message queue*/
        public static final String ROLE_MESSAGE_QUEUE = "role_message_queue:";
    }

    public static class GameLandingRewardsPrefix {
        public static final String GAME_LANDING_REWARDS = "game_landing_rewards:";
    }

    /************************level db***********************/
    public static class UniqueLevelDBPrefix {
        public static final String UNIQUE_NAME = "UNIQUE_NAME:";
    }

}

package com.noseparte.common.global;

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

    public static class BattleRedisPrefix {
        public static final String MATCH_RANK_MODE_QUEUE = "match_rank_mode_queue:";
        public static final String MATCH_QUEUE_BY_ROLE = "match_queue_by_role:";
        public static final String MATCH_LEISURE_MODE_QUEUE = "match_leisure_mode_queue:";
        public static final String BATTLE_ROOM = "battle_room:";
        public static final String BATTLE_ROOM_BY_ROLE = "battle_room_by_role:";
        public static final String MATCH_SESSION = "match_session:";
        public static final String MATCH_SESSION_BY_ROLE = "match_session_by_role:";
        public static final String BATTLE_SERVER_SERVICE = "battle_server_service:";
        public static final String BATTLE_SERVER_SERVICE_LIST = "battle_server_service_list";
    }

    public static final String REDISSON_LOCK = "redisson_lock_";

    /************************level db***********************/
    public static class UniqueLevelDBPrefix {
        public static final String UNIQUE_NAME = "UNIQUE_NAME:";
    }

}

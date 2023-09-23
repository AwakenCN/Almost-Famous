package com.lung.game.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haoyitao
 * @implSpec 玩家业务数据，持久化，跨服
 * @since 2023/9/22 - 16:35
 * @version 1.0
 */
@Getter
@Setter
public class UserProfile {

    private String uid;
    private String username;
    private long gold;
    private int diamond;
    private int age;
    private int sex;
    private int lastOnlineTime;
    private boolean dirty;

    public static UserProfile getLoggedUserProfile(String uid, boolean checkLock) {
        return null;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void createUserData(String uid) {
        setUid(uid);
    }
}

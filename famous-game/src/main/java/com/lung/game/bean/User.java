package com.lung.game.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haoyitao
 * @implSpec 玩家
 * @since 2023/9/1 - 17:29
 * @version 1.0
 */
@Getter
@Setter
public class User {

    private String name;
    private boolean isDirty;

    public static User getLoggedUserProfile(String uid, boolean checkLock) {
        return null;
    }

//    public static User getLoggedUserProfile(String uid, boolean checkLock) {
//        return null;
//    }
}

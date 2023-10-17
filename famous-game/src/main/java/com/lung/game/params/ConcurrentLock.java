package com.lung.game.params;

import com.lung.game.constans.ServerConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * @author noseparte
 * @implSpec 业务锁
 * @since 2023/8/29 - 10:37
 * @version 1.0
 */
public class ConcurrentLock {
    private static final String LOCK_PREFIX = "almost-famous#" + ServerConstant.SERVER_ID; //sid

    public enum LockType {
        LOGIN_CONCURRENT_LOCK("_login_concurrent_lock_"),			//登录互斥锁

		;

        private final String lockCode;

		LockType(String code) {
			lockCode = code;
		}

		public String getLockCode() {
			return lockCode;
		}

	}

    private static class LazyHolder {
        final private static ConcurrentLock INSTANCE = new ConcurrentLock();
    }

	public static ConcurrentLock getInstance() {
        return LazyHolder.INSTANCE;
    }

    public String getKey(LockType lockType, String uid) {
        StringBuilder sb = new StringBuilder(LOCK_PREFIX).append(lockType.getLockCode());
        if (!StringUtils.isBlank(uid)) {
            sb.append(uid);
        }
        return sb.toString();
    }

}

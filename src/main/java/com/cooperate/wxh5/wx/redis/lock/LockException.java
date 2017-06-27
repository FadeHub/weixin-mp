package com.cooperate.wxh5.wx.redis.lock;

import com.cooperate.wxh5.wx.exception.CommonRuntimeException;

/**
 * 锁异常 
 * 
 * @author sunburst
 *
 */
public class LockException extends CommonRuntimeException {

    private static final long serialVersionUID = -5923955899390719980L;

    public LockException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public LockException(int errorCode, String message) {
        super(errorCode, message);
    }

    public LockException(int errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public LockException(int errorCode) {
        super(errorCode);
    }

    public LockException(int errorCode, Object... args) {
        super(errorCode, args);
    }

    public LockException(int errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }

    public LockException(int errorCode, Throwable cause, Object... args) {
        super(errorCode, cause, args);
    }

    public LockException(int errorCode, String message, Throwable cause, Object... args) {
        super(errorCode, message, cause, args);
    }

}

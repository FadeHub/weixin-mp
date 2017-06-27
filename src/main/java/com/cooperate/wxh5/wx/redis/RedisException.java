package com.cooperate.wxh5.wx.redis;

import com.cooperate.wxh5.wx.exception.CommonRuntimeException;

public class RedisException extends CommonRuntimeException {
    private static final long serialVersionUID = 6842769011974511637L;

    public RedisException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public RedisException(int errorCode, String message) {
        super(errorCode, message);
    }

    public RedisException(int errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public RedisException(int errorCode) {
        super(errorCode);
    }

    public RedisException(int errorCode, Object... args) {
        super(errorCode, args);
    }

    public RedisException(int errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }

    public RedisException(int errorCode, Throwable cause, Object... args) {
        super(errorCode, cause, args);
    }

    public RedisException(int errorCode, String message, Throwable cause, Object... args) {
        super(errorCode, message, cause, args);
    }

}

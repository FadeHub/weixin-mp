package com.cooperate.wxh5.wx.exception;


/**
 * 通用运行时异常
 * 
 * @author sunburst
 */
public class CommonRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 3909753914696899192L;

    private int errorCode;

    private String message;

    /**
     * 格式化errorCode对应msg时的参数，此msg用于向用户展示，非成员变量message
     * 其set方法接受可变参数以易于使用，推荐用构造方法对parameters赋值
     */
    private Object[] parameters;

    public CommonRuntimeException(int errorCode) {
        this.errorCode = errorCode;
    }

    public CommonRuntimeException(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public CommonRuntimeException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CommonRuntimeException(int errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public CommonRuntimeException(int errorCode, Object... args) {
        this.errorCode = errorCode;
        this.parameters = args;
    }

    public CommonRuntimeException(int errorCode, String message, Object... args) {
        this.errorCode = errorCode;
        this.message = message;
        this.parameters = args;
    }

    public CommonRuntimeException(int errorCode, Throwable cause, Object... args) {
        super(cause);
        this.errorCode = errorCode;
        this.parameters = args;
    }

    public CommonRuntimeException(int errorCode, String message, Throwable cause, Object... args) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
        this.parameters = args;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        if (null == message) {
            return String.valueOf(this.errorCode);
        }
        return this.errorCode + " | msg=" + message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object... parameters) {
        this.parameters = parameters;
    }

    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + ": EC = " + errorCode + ": MSG = " + message) : s;
    }
}

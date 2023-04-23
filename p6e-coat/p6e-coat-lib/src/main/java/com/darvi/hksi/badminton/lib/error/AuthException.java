package com.darvi.hksi.badminton.lib.error;

/**
 * 自定义异常
 * 未认证异常
 *
 * @author lidashuang
 * @version 1.0
 */
public class AuthException extends CustomException {

    /**
     * 默认的代码
     */
    public static final int DEFAULT_CODE = 2000;

    /**
     * 默认的简述
     */
    private static final String DEFAULT_SKETCH = "AUTH_EXCEPTION";

    /**
     * 未认证异常
     *
     * @param sc      源 class
     * @param content 异常内容
     */
    public AuthException(Class<?> sc, String content) {
        super(sc, AuthException.class, content, DEFAULT_CODE, DEFAULT_SKETCH);
    }

    /**
     * 未认证异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     */
    public AuthException(Class<?> sc, Throwable throwable) {
        super(sc, AuthException.class, throwable, DEFAULT_CODE, DEFAULT_SKETCH);
    }

    /**
     * 未认证异常
     *
     * @param sc      源 class
     * @param content 异常内容
     * @param code    代码
     * @param sketch  简述
     */
    public AuthException(Class<?> sc, String content, int code, String sketch) {
        super(sc, AuthException.class, content, code, sketch);
    }

    /**
     * 未认证异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     * @param code      代码
     * @param sketch    简述
     */
    public AuthException(Class<?> sc, Throwable throwable, int code, String sketch) {
        super(sc, AuthException.class, throwable, code, sketch);
    }
}

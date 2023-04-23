package com.darvi.hksi.badminton.lib.error;

/**
 * 自定义异常
 * 账号密码错误异常
 *
 * @author lidashuang
 * @version 1.0
 */
public class AccountOrPasswordException extends CustomException {

    /**
     * 默认的代码
     */
    public static final int DEFAULT_CODE = 4000;

    /**
     * 默认的简述
     */
    private static final String DEFAULT_SKETCH = "ACCOUNT_OR_PASSWORD_EXCEPTION";

    /**
     * 账号密码异常
     *
     * @param sc      源 class
     * @param content 异常内容
     */
    public AccountOrPasswordException(Class<?> sc, String content) {
        super(sc, AccountOrPasswordException.class, content, DEFAULT_CODE, DEFAULT_SKETCH);
    }

    /**
     * 账号密码异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     */
    public AccountOrPasswordException(Class<?> sc, Throwable throwable) {
        super(sc, AccountOrPasswordException.class, throwable, DEFAULT_CODE, DEFAULT_SKETCH);
    }

    /**
     * 账号密码异常
     *
     * @param sc      源 class
     * @param content 异常内容
     * @param code    代码
     * @param sketch  简述
     */
    public AccountOrPasswordException(Class<?> sc, String content, int code, String sketch) {
        super(sc, AccountOrPasswordException.class, content, code, sketch);
    }

    /**
     * 账号密码异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     * @param code      代码
     * @param sketch    简述
     */
    public AccountOrPasswordException(Class<?> sc, Throwable throwable, int code, String sketch) {
        super(sc, AccountOrPasswordException.class, throwable, code, sketch);
    }
}

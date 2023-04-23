package com.darvi.hksi.badminton.lib.error;

/**
 * 自定义异常
 * 数据库异常
 *
 * @author lidashuang
 * @version 1.0
 */
public class DatabaseException extends CustomException {

    /**
     * 默认的代码
     */
    public static final int DEFAULT_CODE = 7000;

    /**
     * 默认的简述
     */
    private static final String DEFAULT_SKETCH = "DATABASE_EXCEPTION";

    /**
     * 数据库异常
     *
     * @param sc      源 class
     * @param content 异常内容
     */
    public DatabaseException(Class<?> sc, String content) {
        super(sc, DatabaseException.class, content, DEFAULT_CODE, DEFAULT_SKETCH);
    }

    /**
     * 数据库异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     */
    public DatabaseException(Class<?> sc, Throwable throwable) {
        super(sc, DatabaseException.class, throwable, DEFAULT_CODE, DEFAULT_SKETCH);
    }

    /**
     * 数据库异常
     *
     * @param sc      源 class
     * @param content 异常内容
     * @param code    代码
     * @param sketch  简述
     */
    public DatabaseException(Class<?> sc, String content, int code, String sketch) {
        super(sc, DatabaseException.class, content, code, sketch);
    }

    /**
     * 数据库异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     * @param code      代码
     * @param sketch    简述
     */
    public DatabaseException(Class<?> sc, Throwable throwable, int code, String sketch) {
        super(sc, DatabaseException.class, throwable, code, sketch);
    }
}


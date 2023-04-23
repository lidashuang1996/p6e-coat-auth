package com.darvi.hksi.badminton.lib.error;

/**
 * 自定义异常
 * 操作权限异常
 *
 * @author lidashuang
 * @version 1.0
 */
public class PermissionException extends CustomException {

    /**
     * 默认的代码
     */
    public static final int DEFAULT_CODE = 3000;

    /**
     * 默认的简述
     */
    private static final String DEFAULT_SKETCH = "PERMISSION_EXCEPTION";

    /**
     * 操作权限异常
     *
     * @param sc      源 class
     * @param content 异常内容
     */
    public PermissionException(Class<?> sc, String content) {
        super(sc, PermissionException.class, content, DEFAULT_CODE, DEFAULT_SKETCH);
    }

    /**
     * 操作权限异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     */
    public PermissionException(Class<?> sc, Throwable throwable) {
        super(sc, PermissionException.class, throwable, DEFAULT_CODE, DEFAULT_SKETCH);
    }


    /**
     * 操作权限异常
     *
     * @param sc      源 class
     * @param content 异常内容
     * @param code    代码
     * @param sketch  简述
     */
    public PermissionException(Class<?> sc, String content, int code, String sketch) {
        super(sc, PermissionException.class, content, code, sketch);
    }

    /**
     * 操作权限异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     * @param code      代码
     * @param sketch    简述
     */
    public PermissionException(Class<?> sc, Throwable throwable, int code, String sketch) {
        super(sc, PermissionException.class, throwable, code, sketch);
    }
}

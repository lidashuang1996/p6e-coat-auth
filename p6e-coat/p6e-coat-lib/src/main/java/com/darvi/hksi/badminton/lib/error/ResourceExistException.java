package com.darvi.hksi.badminton.lib.error;

/**
 * 自定义异常
 * 资源异常
 *
 * @author lidashuang
 * @version 1.0
 */
public class ResourceExistException extends CustomException {

    /**
     * 默认的代码
     */
    public static final int DEFAULT_CODE = 9000;

    /**
     * 默认的简述
     */
    private static final String DEFAULT_SKETCH = "RESOURCE_EXIST_EXCEPTION";

    /**
     * 资源异常
     *
     * @param sc      源 class
     * @param content 异常内容
     */
    public ResourceExistException(Class<?> sc, String content) {
        super(sc, ResourceExistException.class, content, DEFAULT_CODE, DEFAULT_SKETCH);
    }

    /**
     * 资源异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     */
    public ResourceExistException(Class<?> sc, Throwable throwable) {
        super(sc, ResourceExistException.class, throwable, DEFAULT_CODE, DEFAULT_SKETCH);
    }

    /**
     * 资源异常
     *
     * @param sc      源 class
     * @param content 异常内容
     * @param code    代码
     * @param sketch  简述
     */
    public ResourceExistException(Class<?> sc, String content, int code, String sketch) {
        super(sc, ResourceExistException.class, content, code, sketch);
    }

    /**
     * 资源异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     * @param code      代码
     * @param sketch    简述
     */
    public ResourceExistException(Class<?> sc, Throwable throwable, int code, String sketch) {
        super(sc, ResourceExistException.class, throwable, code, sketch);
    }
}


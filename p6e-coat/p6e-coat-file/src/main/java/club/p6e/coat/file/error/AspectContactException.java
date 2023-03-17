package club.p6e.coat.file.error;

/**
 * 自定义异常
 * 请求参数参数异常
 *
 * @author lidashuang
 * @version 1.0
 */
public class AspectContactException extends CustomException {

    /**
     * 默认的代码
     */
    public static final int DEFAULT_CODE = 1000;

    /**
     * 默认的简述
     */
    private static final String DEFAULT_SKETCH = "PARAMETER_EXCEPTION";

    /**
     * 请求参数参数异常
     *
     * @param sc      源 class
     * @param content 异常内容
     */
    public AspectContactException(Class<?> sc, String content) {
        super(sc, AspectContactException.class, content, DEFAULT_CODE, DEFAULT_SKETCH);
    }

    /**
     * 请求参数参数异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     */
    public AspectContactException(Class<?> sc, Throwable throwable) {
        super(sc, AspectContactException.class, throwable, DEFAULT_CODE, DEFAULT_SKETCH);
    }

    /**
     * 请求参数参数异常
     *
     * @param sc      源 class
     * @param content 异常内容
     * @param code    代码
     * @param sketch  简述
     */
    public AspectContactException(Class<?> sc, String content, int code, String sketch) {
        super(sc, AspectContactException.class, content, code, sketch);
    }

    /**
     * 请求参数参数异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     * @param code      代码
     * @param sketch    简述
     */
    public AspectContactException(Class<?> sc, Throwable throwable, int code, String sketch) {
        super(sc, AspectContactException.class, throwable, code, sketch);
    }
}

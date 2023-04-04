package club.p6e.coat.file.error;

/**
 * 自定义异常
 * 网络 HTTP 媒体类型异常
 *
 * @author lidashuang
 * @version 1.0
 */
public class HttpMediaTypeException extends CustomException {

    /**
     * 默认的代码
     */
    public static final int DEFAULT_CODE = 5000;

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
    public HttpMediaTypeException(Class<?> sc, String error, String content) {
        super(sc, HttpMediaTypeException.class, error, DEFAULT_CODE, DEFAULT_SKETCH, content);
    }

    /**
     * 请求参数参数异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     */
    public HttpMediaTypeException(Class<?> sc, Throwable throwable, String content) {
        super(sc, HttpMediaTypeException.class, throwable, DEFAULT_CODE, DEFAULT_SKETCH, content);
    }

    /**
     * 请求参数参数异常
     *
     * @param sc      源 class
     * @param content 异常内容
     * @param code    代码
     * @param sketch  简述
     */
    public HttpMediaTypeException(Class<?> sc, String error, int code, String sketch, String content) {
        super(sc, HttpMediaTypeException.class, error, code, sketch, content);
    }

    /**
     * 请求参数参数异常
     *
     * @param sc        源 class
     * @param throwable 异常对象
     * @param code      代码
     * @param sketch    简述
     */
    public HttpMediaTypeException(Class<?> sc, Throwable throwable, int code, String sketch, String content) {
        super(sc, HttpMediaTypeException.class, throwable, code, sketch, content);
    }
}

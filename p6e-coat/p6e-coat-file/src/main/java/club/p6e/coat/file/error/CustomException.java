package club.p6e.coat.file.error;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常
 *
 * @author lidashuang
 * @version 1.0
 */
public abstract class CustomException extends RuntimeException {

    /**
     * 转型器缓存
     */
    private static final Map<Class<? extends Exception>, CustomException> TRANSFORMER_CACHE = new HashMap<>();

    /**
     * 代码
     */
    private final int code;

    /**
     * 标记
     */
    private final String sketch;

    /**
     * 转换
     *
     * @param exception 异常对象
     * @return 异常对象
     */
    public static Exception transformation(Exception exception) {
        final CustomException customException = TRANSFORMER_CACHE.get(exception.getClass());
        return customException == null ? exception : customException;
    }

    /**
     * 转换器
     *
     * @param ec              异常对象 class
     * @param customException 自定义异常对象
     */
    public synchronized static void registerTransformer(Class<? extends Exception> ec, CustomException customException) {
        TRANSFORMER_CACHE.put(ec, customException);
    }

    /**
     * 模版输出
     *
     * @param sc      源 class
     * @param ec      异常 class
     * @param content 错误的描述
     * @param code    代码
     * @param sketch  简述
     * @return 模版的输出内容
     */
    private static String template(Class<?> sc, Class<? extends CustomException> ec, String content, int code, String sketch) {
        return "{ " + sketch + " <" + code + "> } ::: [ " + sc + " ] => (" + ec + ") ===> " + content;
    }

    /**
     * 构造方法
     *
     * @param sc      源 class
     * @param ec      异常 class
     * @param content 错误的描述
     * @param code    代码
     * @param sketch  简述
     */
    public CustomException(Class<?> sc, Class<? extends CustomException> ec, String content, int code, String sketch) {
        super(template(sc, ec, content, code, sketch));
        this.code = code;
        this.sketch = sketch;
    }

    /**
     * 构造方法
     *
     * @param sc        源 class
     * @param ec        异常 class
     * @param throwable 异常对象
     * @param code      代码
     * @param sketch    简述
     */
    public CustomException(Class<?> sc, Class<? extends CustomException> ec, Throwable throwable, int code, String sketch) {
        super(template(sc, ec, throwable.getMessage(), code, sketch), throwable);
        this.code = code;
        this.sketch = sketch;
    }

    /**
     * 获取代码
     *
     * @return 代码内容
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取简述
     *
     * @return 简述内容
     */
    public String getSketch() {
        return sketch;
    }
}

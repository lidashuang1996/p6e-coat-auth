package com.example.p6e_dawenjian_2023.mapper;

import com.example.p6e_dawenjian_2023.utils.SpringUtil;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 请求参数映射器
 *
 * @author lidashuang
 * @version 1.0
 */
public abstract class RequestParameterMapper {

    /**
     * 表单数据前缀
     */
    public static final String FORM_DATA_PREFIX = "$FD_";

    /**
     * 参数数据前缀
     */
    public static final String PARAMETER_DATA_PREFIX = "$PR_";

    /**
     * 是否刷新缓存
     */
    private static boolean IS_REFRESH_CACHE = false;

    /**
     * 映射器缓存
     */
    private static final Map<Class<?>, RequestParameterMapper> CACHE = new Hashtable<>();

    /**
     * 获取映射后的数据类型对象
     *
     * @param request ServerRequest 对象
     * @param oClass  映射的数据类型
     * @param <T>     映射的数据类型
     * @return 映射的数据类型对象的泛型
     */
    @SuppressWarnings("all")
    public static <T> Mono<T> execute(ServerRequest request, Class<T> oClass) {
        if (!IS_REFRESH_CACHE) {
            refresh();
            IS_REFRESH_CACHE = true;
        }
        final RequestParameterMapper mapper = CACHE.get(oClass);
        if (mapper == null) {
            throw new NullPointerException(RequestParameterMapper.class
                    + " execute(). CACHE.get(" + oClass.getName() + ") => mapper is null !");
        } else {
            return mapper
                    .execute(request)
                    .map(o -> {
                        if (o.getClass() == oClass) {
                            return (T) o;
                        } else {
                            throw new TypeMismatchException(o, oClass);
                        }
                    });
        }
    }

    /**
     * 刷新映射的数据类型对象缓存
     */
    public static synchronized void refresh() {
        CACHE.clear();
        final Map<String, RequestParameterMapper> beans = SpringUtil.getBeans(RequestParameterMapper.class);
        for (RequestParameterMapper value : beans.values()) {
            CACHE.put(value.outputClass(), value);
        }
    }

    /**
     * 读取 FormData 里面的数据
     *
     * @param request ServerRequest 对象
     * @param data    保存的结果的数据对象
     * @return 结果的数据对象
     */
    @SuppressWarnings("all")
    public static Mono<Map<String, Object>> requestFormDataMapper(ServerRequest request, Map<String, Object> data) {
        return request
                .exchange()
                .getMultipartData()
                .map(m -> {
                    for (final String key : m.keySet()) {
                        if (data.get(FORM_DATA_PREFIX + key) != null) {
                            data.put(PARAMETER_DATA_PREFIX + FORM_DATA_PREFIX + key, data.get(FORM_DATA_PREFIX + key));
                            data.remove(FORM_DATA_PREFIX + key);
                        }
                        for (final Part part : m.get(key)) {
                            data.putIfAbsent(FORM_DATA_PREFIX + key, new ArrayList<>());
                            ((List<Object>) data.get(FORM_DATA_PREFIX + key)).add(
                                    part instanceof final FormFieldPart fieldPart ? fieldPart.value() : part
                            );
                        }
                    }
                    return data;
                });
    }

    /**
     * 输出映射的数据类型
     *
     * @return 映射的数据类型
     */
    public abstract Class<?> outputClass();

    /**
     * 执行映射操作
     *
     * @return 映射的数据类型对象
     */
    public abstract Mono<Object> execute(ServerRequest request);

}

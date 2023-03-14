package com.example.p6e_dawenjian_2023.mapper;

import com.example.p6e_dawenjian_2023.utils.SpringUtil;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import java.util.Hashtable;
import java.util.Map;

/**
 * 请求参数映射器
 *
 * @author lidashuang
 * @version 1.0
 */
public abstract class RequestParameterMapper {

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
     * @param request ServerHttpRequest 对象
     * @param oClass  映射的数据类型
     * @param <T>     映射的数据类型
     * @return 映射的数据类型对象的泛型
     */
    @SuppressWarnings("all")
    public static <T> Mono<T> execute(ServerHttpRequest request, Class<T> oClass) {
        if (!IS_REFRESH_CACHE) {
            refresh();
            IS_REFRESH_CACHE = true;
        }
        final RequestParameterMapper mapper = CACHE.get(oClass);
        if (mapper == null) {
            throw new NullPointerException(RequestParameterMapper.class
                    + " execute(). CACHE.get(" + oClass.getName() + ") => mapper is null !");
        } else {
            final Object o = mapper.execute(request);
            if (o.getClass() == oClass) {
                return Mono.just((T) o);
            } else {
                throw new TypeMismatchException(o, oClass);
            }
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
    public abstract Mono<Object> execute(ServerHttpRequest request);

}

package com.example.p6e_dawenjian_2023.aspect;

import com.example.p6e_dawenjian_2023.utils.FileUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 切面（钩子）的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class DefaultOpenUploadAspectImpl implements OpenUploadAspect {

    @Override
    public Mono<Boolean> before(Map<String, Object> data) {
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> after(Map<String, Object> data, Map<String, Object> result) {
        final Object id = result.get("id");
        result.clear();
        result.put("id", id);
        return Mono.just(true);
    }

}

package com.example.p6e_dawenjian_2023.aspect;

import com.example.p6e_dawenjian_2023.utils.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@ConditionalOnMissingBean(
        value = OpenUploadAspect.class,
        ignored = DefaultOpenUploadAspectImpl.class
)
public class DefaultSimpleUploadAspectImpl implements SimpleUploadAspect {

    @Override
    public Mono<Boolean> before(Map<String, Object> data) {
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> after(Map<String, Object> data, Map<String, Object> result) {
        final Object id = result.get("id");
        final String fileName = String.valueOf(result.get("name"));
        final String storageLocation = String.valueOf(result.get("storageLocation"));
        result.clear();
        result.put("id", id);
        result.put("path", FileUtil.composePath(storageLocation, fileName));
        return Mono.just(true);
    }

}

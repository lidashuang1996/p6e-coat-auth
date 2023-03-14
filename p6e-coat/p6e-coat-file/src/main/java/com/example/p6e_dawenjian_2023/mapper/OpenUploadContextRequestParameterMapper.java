package com.example.p6e_dawenjian_2023.mapper;

import com.example.p6e_dawenjian_2023.context.OpenUploadContext;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class OpenUploadContextRequestParameterMapper extends RequestParameterMapper {

    @Override
    public Class<?> outputClass() {
        return OpenUploadContext.class;
    }

    @Override
    public Mono<Object> execute(ServerHttpRequest request) {
        final OpenUploadContext context = new OpenUploadContext();
        final MultiValueMap<String, String> queryParams = request.getQueryParams();
        context.putAll(queryParams);
        final List<String> names = request.getQueryParams().get("name");
        if (names != null && names.size() > 0) {
            context.setName(names.get(0));
        }
        return Mono.just(context);
    }

}

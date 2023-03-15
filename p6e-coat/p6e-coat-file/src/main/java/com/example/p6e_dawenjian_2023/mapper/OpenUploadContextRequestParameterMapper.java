package com.example.p6e_dawenjian_2023.mapper;

import com.example.p6e_dawenjian_2023.context.OpenUploadContext;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
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
    public Mono<Object> execute(ServerRequest request) {
        final OpenUploadContext context = new OpenUploadContext();
        final ServerHttpRequest httpRequest = request.exchange().getRequest();
        final MultiValueMap<String, String> queryParams = httpRequest.getQueryParams();
        context.putAll(queryParams);
        final List<String> names = httpRequest.getQueryParams().get("name");
        if (names != null && names.size() > 0) {
            context.setName(names.get(0));
            context.remove("name");
        } else {
            throw new RuntimeException();
        }
        return Mono.just(context);
    }

}

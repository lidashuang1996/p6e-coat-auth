package com.example.p6e_dawenjian_2023.mapper;

import com.example.p6e_dawenjian_2023.context.CloseUploadContext;
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
public class CloseUploadContextRequestParameterMapper extends RequestParameterMapper {

    @Override
    public Class<?> outputClass() {
        return CloseUploadContext.class;
    }

    @Override
    public Mono<Object> execute(ServerRequest request) {
        final CloseUploadContext context = new CloseUploadContext();
        final ServerHttpRequest httpRequest = request.exchange().getRequest();
        final MultiValueMap<String, String> queryParams = httpRequest.getQueryParams();
        context.putAll(queryParams);
        System.out.println(
                httpRequest.getPath().elements()
        );
        return Mono.just(context);
    }

}

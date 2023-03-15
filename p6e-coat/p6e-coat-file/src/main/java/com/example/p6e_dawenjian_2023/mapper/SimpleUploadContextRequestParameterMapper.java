package com.example.p6e_dawenjian_2023.mapper;

import com.example.p6e_dawenjian_2023.context.SimpleUploadContext;
import org.springframework.http.codec.multipart.FilePart;
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
public class SimpleUploadContextRequestParameterMapper extends RequestParameterMapper {

    private static final String FORM_DATA_PARAMETER_FILE = "file";

    @Override
    public Class<?> outputClass() {
        return SimpleUploadContext.class;
    }

    @Override
    public Mono<Object> execute(ServerRequest request) {
        final SimpleUploadContext context = new SimpleUploadContext();
        final ServerHttpRequest httpRequest = request.exchange().getRequest();
        final MultiValueMap<String, String> queryParams = httpRequest.getQueryParams();
        context.putAll(queryParams);
        return requestFormDataMapper(request, context.toMap())
                .map(m -> {
                    final SimpleUploadContext newContext = new SimpleUploadContext(m);
                    final Object o = newContext.get(FORM_DATA_PREFIX + FORM_DATA_PARAMETER_FILE);
                    if (o instanceof final List<?> ol && ol.size() > 0
                            && ol.get(0) instanceof final FilePart filePart) {
                        newContext.setFilePart(filePart);
                        newContext.remove(FORM_DATA_PREFIX + FORM_DATA_PARAMETER_FILE);
                    } else {
                        throw new RuntimeException();
                    }
                    return newContext;
                });
    }

}

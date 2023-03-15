package com.example.p6e_dawenjian_2023.mapper;

import com.example.p6e_dawenjian_2023.context.OpenUploadContext;
import com.example.p6e_dawenjian_2023.error.ParameterException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 打开上传请求参数映射器
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class OpenUploadContextRequestParameterMapper extends RequestParameterMapper {

    /**
     * URL 文件名称请求参数
     */
    private static final String URL_PARAMETER_NAME = "name";

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
        // 读取 URL 文件名称请求参数
        final List<String> names = queryParams.get(URL_PARAMETER_NAME);
        if (names != null && names.size() > 0) {
            // 如果读取到了 URL 文件请求参数那么就写入到上下文对象中
            context.setName(names.get(0));
        } else {
            // 如果没有读取到了 URL 文件请求参数那么就抛出参数异常
            throw new ParameterException(this.getClass(),
                    "<" + URL_PARAMETER_NAME + "> request parameter exception");
        }
        return Mono.just(context);
    }

}

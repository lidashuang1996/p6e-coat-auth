package com.example.p6e_dawenjian_2023.mapper;

import com.example.p6e_dawenjian_2023.context.CloseUploadContext;
import com.example.p6e_dawenjian_2023.error.ParameterException;
import org.springframework.http.server.PathContainer;
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

    private static final String REQUEST_PATH_FINISH_MARK = "close";

    /**
     * URL ID 请求参数
     */
    private static final String URL_PARAMETER_ID = "id";

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
        final List<PathContainer.Element> elements = request.requestPath().elements();
        final String requestPathFinishContent = elements.get(elements.size() - 1).value();
        if (!REQUEST_PATH_FINISH_MARK.equals(requestPathFinishContent)) {
            try {
                context.setId(Integer.valueOf(requestPathFinishContent));
            } catch (Exception e) {
                // 忽略异常
            }
        }
        if (context.getId() == null) {
            // 读取 URL ID 请求参数
            final List<String> names = queryParams.get(URL_PARAMETER_ID);
            if (names != null && names.size() > 0) {
                try {
                    // 如果读取到了 URL ID 那么就写入到上下文对象中
                    context.setId(Integer.valueOf(names.get(0)));
                } catch (Exception e) {
                    // 如果没有读取到了 URL ID 那么就抛出参数异常
                    throw new ParameterException(this.getClass(),
                            "<" + URL_PARAMETER_ID + "> request parameter type is not int");
                }
            } else {
                // 如果没有读取到了 URL ID 那么就抛出参数异常
                throw new ParameterException(this.getClass(),
                        "<" + URL_PARAMETER_ID + "> request parameter exception");
            }
        }
        return Mono.just(context);
    }

}

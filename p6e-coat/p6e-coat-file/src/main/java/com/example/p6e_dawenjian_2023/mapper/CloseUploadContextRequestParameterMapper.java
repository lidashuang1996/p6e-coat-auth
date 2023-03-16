package com.example.p6e_dawenjian_2023.mapper;

import com.example.p6e_dawenjian_2023.context.CloseUploadContext;
import com.example.p6e_dawenjian_2023.context.SliceUploadContext;
import com.example.p6e_dawenjian_2023.error.ParameterException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 关闭上传请求参数映射器
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(CloseUploadContextRequestParameterMapper.class)
public class CloseUploadContextRequestParameterMapper extends RequestParameterMapper {

    /**
     * URL ID 请求参数
     */
    private static final String URL_PARAMETER_ID = "id";

    /**
     * URL ID 请求参数
     */
    private static final String FORM_DATA_PARAMETER_ID = "id";

    /**
     * 请求路径后缀标记
     */
    private static final String REQUEST_PATH_FINISH_MARK = "close";

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
        // 如果不是请求后缀标记
        // 那么请求的后缀是请求参数 ID
        if (!REQUEST_PATH_FINISH_MARK.equals(requestPathFinishContent)) {
            try {
                context.setId(Integer.valueOf(requestPathFinishContent));
            } catch (Exception e) {
                // 忽略异常
            }
        }
        // 如果请求参数 ID 还是不存在，那么就去请求的路径中查询
        if (context.getId() == null) {
            // 读取 URL ID 请求参数
            final List<String> names = queryParams.get(URL_PARAMETER_ID);
            if (names != null && names.size() > 0) {
                try {
                    // 如果读取到了 URL ID 那么就写入到上下文对象中
                    context.setId(Integer.valueOf(names.get(0)));
                    return Mono.just(context);
                } catch (Exception e) {
                    // 如果没有读取到了 URL ID 那么就抛出参数异常
                    throw new ParameterException(this.getClass(),
                            "<" + URL_PARAMETER_ID + "> request parameter type is not int");
                }
            } else {
                final MediaType mediaType = httpRequest.getHeaders().getContentType();
                if (MediaType.APPLICATION_JSON == mediaType) {
                    return requestRawJsonMapper(request, context)
                            .map(m -> {
                                final SliceUploadContext newContext = new SliceUploadContext(m);
                                final Object fdId = newContext.get(FORM_DATA_PREFIX + FORM_DATA_PARAMETER_ID);
                                if (fdId instanceof final List<?> ol && ol.size() > 0
                                        && ol.get(0) instanceof final FormFieldPart filePart) {
                                    try {
                                        newContext.setId(Integer.valueOf(filePart.value()));
                                        return newContext;
                                    } catch (Exception e) {
                                        // 如果没有读取到了 URL ID 那么就抛出参数异常
                                        throw new ParameterException(this.getClass(),
                                                "<" + FORM_DATA_PARAMETER_ID + "> request parameter type is not int");
                                    }
                                }
                                // 如果没有读取到了 FORM DATA 文件请求参数那么就抛出参数异常
                                throw new ParameterException(this.getClass(),
                                        "<" + FORM_DATA_PARAMETER_ID + "> request parameter is null");

                            });
                } else if (MediaType.MULTIPART_FORM_DATA == mediaType) {
                    return requestFormDataMapper(request, context)
                            .map(m -> {
                                final SliceUploadContext newContext = new SliceUploadContext(m);
                                final Object fdId = newContext.get(FORM_DATA_PREFIX + FORM_DATA_PARAMETER_ID);
                                if (fdId instanceof final List<?> ol && ol.size() > 0
                                        && ol.get(0) instanceof final FormFieldPart filePart) {
                                    try {
                                        newContext.setId(Integer.valueOf(filePart.value()));
                                        return newContext;
                                    } catch (Exception e) {
                                        // 如果没有读取到了 URL ID 那么就抛出参数异常
                                        throw new ParameterException(this.getClass(),
                                                "<" + FORM_DATA_PARAMETER_ID + "> request parameter type is not int");
                                    }
                                }
                                // 如果没有读取到了 FORM DATA 文件请求参数那么就抛出参数异常
                                throw new ParameterException(this.getClass(),
                                        "<" + FORM_DATA_PARAMETER_ID + "> request parameter is null");

                            });
                } else {
                    throw new RuntimeException();
                }
            }
        }
    }

}

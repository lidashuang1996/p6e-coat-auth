package com.example.p6e_dawenjian_2023.mapper;

import com.example.p6e_dawenjian_2023.context.OpenUploadContext;
import com.example.p6e_dawenjian_2023.error.HttpMediaTypeException;
import com.example.p6e_dawenjian_2023.error.ParameterException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
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
@ConditionalOnMissingBean(
        value = OpenUploadContextRequestParameterMapper.class,
        ignored = OpenUploadContextRequestParameterMapper.class
)
public class OpenUploadContextRequestParameterMapper extends RequestParameterMapper {

    /**
     * URL 文件名称请求参数
     */
    private static final String URL_PARAMETER_NAME = "name";

    /**
     * FORM DATA 文件名称请求参数
     */
    private static final String FORM_DATA_PARAMETER_NAME = "name";

    /**
     * RAW JSON 文件名称请求参数
     */
    private static final String RAW_JSON_PARAMETER_NAME = "name";

    @Override
    public Class<?> outputClass() {
        return OpenUploadContext.class;
    }

    @Override
    public Mono<Object> execute(@RequestBody ServerRequest request) {
        final OpenUploadContext context = new OpenUploadContext();
        final ServerHttpRequest httpRequest = request.exchange().getRequest();
        final MultiValueMap<String, String> queryParams = httpRequest.getQueryParams();
        context.putAll(queryParams);
        // 读取 URL 文件名称请求参数
        final List<String> names = queryParams.get(URL_PARAMETER_NAME);
        if (names != null && names.size() > 0) {
            // 如果读取到了 URL 文件名称请求参数那么就写入到上下文对象中
            context.setName(names.get(0));
            return Mono.just(context);
        } else {
            // 读取请求的媒体类型
            final MediaType mediaType = httpRequest.getHeaders().getContentType();
            if (MediaType.APPLICATION_JSON == mediaType) {
                return requestRawJsonMapper(request, context)
                        .map(m -> {
                            final OpenUploadContext newContext = new OpenUploadContext(m);
                            final Object rjName = newContext.get(RAW_JSON_PREFIX + RAW_JSON_PARAMETER_NAME);
                            if (rjName instanceof final String content) {
                                newContext.setName(content);
                                return newContext;
                            }
                            // 如果没有读取到了 RAW JSON 文件名称请求参数那么就抛出参数异常
                            throw new ParameterException(this.getClass(),
                                    "<" + RAW_JSON_PARAMETER_NAME + "> request parameter is null");
                        });
            } else if (MediaType.MULTIPART_FORM_DATA == mediaType) {
                return requestFormDataMapper(request, context)
                        .map(m -> {
                            final OpenUploadContext newContext = new OpenUploadContext(m);
                            final Object fdName = newContext.get(FORM_DATA_PREFIX + FORM_DATA_PARAMETER_NAME);
                            if (fdName instanceof final List<?> ol && ol.size() > 0
                                    && ol.get(0) instanceof final FormFieldPart fieldPart) {
                                newContext.setName(fieldPart.value());
                            }
                            // 如果没有读取到了 FORM DATA 文件名称请求参数那么就抛出参数异常
                            throw new ParameterException(this.getClass(),
                                    "<" + FORM_DATA_PARAMETER_NAME + "> request parameter is null");

                        });
            } else {
                throw new HttpMediaTypeException(this.getClass(), "Unrecognized media type [" + mediaType + "]");
            }
        }
    }

}

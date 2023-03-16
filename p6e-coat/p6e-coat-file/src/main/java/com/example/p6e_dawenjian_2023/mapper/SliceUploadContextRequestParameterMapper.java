package com.example.p6e_dawenjian_2023.mapper;

import com.example.p6e_dawenjian_2023.context.SliceUploadContext;
import com.example.p6e_dawenjian_2023.error.ParameterException;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 分片上传请求参数映射器
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class SliceUploadContextRequestParameterMapper extends RequestParameterMapper {

    /**
     * URL ID 请求参数
     */
    private static final String URL_PARAMETER_ID = "id";

    /**
     * URL INDEX 请求参数
     */
    private static final String URL_PARAMETER_INDEX = "index";

    /**
     * URL SIGNATURE 请求参数
     */
    private static final String URL_PARAMETER_SIGNATURE = "signature";

    /**
     * FORM ID 请求参数
     */
    private static final String FORM_DATA_PARAMETER_ID = "id";

    /**
     * FORM INDEX 请求参数
     */
    private static final String FORM_DATA_PARAMETER_INDEX = "index";

    /**
     * FORM SIGNATURE 请求参数
     */
    private static final String FORM_DATA_PARAMETER_SIGNATURE = "signature";

    /**
     * FORM FILE 请求参数
     */
    private static final String FORM_DATA_PARAMETER_FILE = "file";

    /**
     * 请求路径后缀标记
     */
    private static final String REQUEST_PATH_FINISH_MARK = "slice";

    @Override
    public Class<?> outputClass() {
        return SliceUploadContext.class;
    }

    @Override
    public Mono<Object> execute(ServerRequest request) {
        final SliceUploadContext context = new SliceUploadContext();
        final ServerHttpRequest httpRequest = request.exchange().getRequest();
        final MultiValueMap<String, String> queryParams = httpRequest.getQueryParams();
        final MediaType mediaType = httpRequest.getHeaders().getContentType();
        if (MediaType.MULTIPART_FORM_DATA != mediaType) {
            throw new RuntimeException();
        }
        context.putAll(queryParams);
        // 初始化请求参数 ID
        initParameterId(request, context);
        // 初始化请求参数 INDEX
        initParameterIndex(request, context);
        // 初始化请求参数 SIGNATURE
        initParameterSignature(request, context);
        // FROM DATA 参数
        return requestFormDataMapper(request, context.toMap())
                .map(m -> {
                    final SliceUploadContext newContext = new SliceUploadContext(m);
                    if (newContext.getId() == null) {
                        final Object fdId = newContext.get(FORM_DATA_PREFIX + FORM_DATA_PARAMETER_ID);
                        if (fdId instanceof final List<?> ol && ol.size() > 0
                                && ol.get(0) instanceof final FormFieldPart filePart) {
                            try {
                                newContext.setId(Integer.valueOf(filePart.value()));
                            } catch (Exception e) {
                                // 如果没有读取到了 URL ID 那么就抛出参数异常
                                throw new ParameterException(this.getClass(),
                                        "<" + FORM_DATA_PARAMETER_ID + "> request parameter type is not int");
                            }
                        } else {
                            // 如果没有读取到了 FORM DATA 文件请求参数那么就抛出参数异常
                            throw new ParameterException(this.getClass(),
                                    "<" + FORM_DATA_PARAMETER_ID + "> request parameter is null");
                        }
                    }
                    if (newContext.getIndex() == null) {
                        final Object fdIndex = newContext.get(FORM_DATA_PREFIX + FORM_DATA_PARAMETER_INDEX);
                        if (fdIndex instanceof final List<?> ol && ol.size() > 0
                                && ol.get(0) instanceof final FormFieldPart filePart) {
                            try {
                                newContext.setIndex(Integer.valueOf(filePart.value()));
                            } catch (Exception e) {
                                // 如果没有读取到了 URL ID 那么就抛出参数异常
                                throw new ParameterException(this.getClass(),
                                        "<" + FORM_DATA_PARAMETER_INDEX + "> request parameter type is not int");
                            }
                        } else {
                            // 如果没有读取到了 FORM DATA 文件请求参数那么就抛出参数异常
                            throw new ParameterException(this.getClass(),
                                    "<" + FORM_DATA_PARAMETER_INDEX + "> request parameter is null");
                        }
                    }
                    if (newContext.getSignature() == null) {
                        final Object fdSignature = newContext.get(FORM_DATA_PREFIX + FORM_DATA_PARAMETER_SIGNATURE);
                        if (fdSignature instanceof final List<?> ol && ol.size() > 0
                                && ol.get(0) instanceof final FormFieldPart filePart) {
                            newContext.setSignature(filePart.value());
                        } else {
                            // 如果没有读取到了 FORM DATA 文件请求参数那么就抛出参数异常
                            throw new ParameterException(this.getClass(),
                                    "<" + FORM_DATA_PARAMETER_SIGNATURE + "> request parameter is null");
                        }
                    }
                    final Object fdFile = newContext.get(FORM_DATA_PREFIX + FORM_DATA_PARAMETER_FILE);
                    if (fdFile instanceof final List<?> ol
                            && ol.size() > 0 && ol.get(0) instanceof final FilePart fp) {
                        newContext.setFilePart(fp);
                    } else {
                        // 如果没有读取到了 FORM DATA 文件请求参数那么就抛出参数异常
                        throw new ParameterException(this.getClass(),
                                "<" + FORM_DATA_PARAMETER_FILE + "> request parameter is null");
                    }
                    return newContext;
                });
    }

    /**
     * 初始化请求参数 ID
     *
     * @param request ServerRequest 对象
     * @param context SliceUploadContext 对象
     */
    private void initParameterId(ServerRequest request, SliceUploadContext context) {
        final List<PathContainer.Element> elements = request.requestPath().elements();
        final String requestPathFinishContent = elements.get(elements.size() - 1).value();
        final MultiValueMap<String, String> queryParams = request.exchange().getRequest().getQueryParams();
        if (!REQUEST_PATH_FINISH_MARK.equals(requestPathFinishContent)) {
            try {
                context.setId(Integer.valueOf(requestPathFinishContent));
            } catch (Exception e) {
                // 忽略异常
            }
        }
        if (context.getId() == null) {
            // 读取 URL ID 请求参数
            final List<String> ids = queryParams.get(URL_PARAMETER_ID);
            if (ids == null || ids.size() == 0) {
                // 如果没有读取到了 URL ID 那么就抛出参数异常
                throw new ParameterException(this.getClass(),
                        "<" + URL_PARAMETER_ID + "> request parameter is null");
            } else {
                try {
                    // 如果读取到了 URL ID 那么就写入到上下文对象中
                    context.setId(Integer.valueOf(ids.get(0)));
                } catch (Exception e) {
                    // 如果没有读取到了 URL ID 那么就抛出参数异常
                    throw new ParameterException(this.getClass(),
                            "<" + URL_PARAMETER_ID + "> request parameter type is not int");
                }
            }
        }
    }

    /**
     * 初始化请求参数 INDEX
     *
     * @param request ServerRequest 对象
     * @param context SliceUploadContext 对象
     */
    private void initParameterIndex(ServerRequest request, SliceUploadContext context) {
        final MultiValueMap<String, String> queryParams = request.exchange().getRequest().getQueryParams();
        // 读取 URL INDEX 请求参数
        final List<String> indexes = queryParams.get(URL_PARAMETER_INDEX);
        if (indexes == null || indexes.size() == 0) {
            // 如果没有读取到了 URL INDEX 那么就抛出参数异常
            throw new ParameterException(this.getClass(),
                    "<" + URL_PARAMETER_INDEX + "> request parameter is null");
        } else {
            try {
                // 如果读取到了 URL INDEX 那么就写入到上下文对象中
                context.setIndex(Integer.valueOf(indexes.get(0)));
            } catch (Exception e) {
                // 如果没有读取到了 URL INDEX 那么就抛出参数异常
                throw new ParameterException(this.getClass(),
                        "<" + URL_PARAMETER_INDEX + "> request parameter type is not int");
            }
        }
    }

    /**
     * 初始化请求参数 SIGNATURE
     *
     * @param request ServerRequest 对象
     * @param context SliceUploadContext 对象
     */
    private void initParameterSignature(ServerRequest request, SliceUploadContext context) {
        final MultiValueMap<String, String> queryParams = request.exchange().getRequest().getQueryParams();
        // 读取 URL SIGNATURE 请求参数
        final List<String> signatures = queryParams.get(URL_PARAMETER_SIGNATURE);
        if (signatures == null || signatures.size() == 0) {
            // 如果没有读取到了 URL SIGNATURE 那么就抛出参数异常
            throw new ParameterException(this.getClass(),
                    "<" + URL_PARAMETER_SIGNATURE + "> request parameter is null");
        } else {
            // 如果读取到了 URL SIGNATURE 那么就写入到上下文对象中
            context.setSignature(signatures.get(0));
        }
    }

}

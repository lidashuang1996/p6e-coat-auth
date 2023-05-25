package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.error.CustomException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.all;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 自定义处理异常
 *
 * @author lidashuang
 * @version 1.0
 */

@Order(-1)
@Component
public class AuthExceptionConfig22 extends DefaultErrorWebExceptionHandler {

    /**
     * 格式化时间对象
     */
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AuthExceptionConfig22(ErrorAttributes errorAttributes,
                                 WebProperties webProperties,
                                 ServerProperties serverProperties,
                                 ApplicationContext applicationContext,
                                 ObjectProvider<ViewResolver> viewResolvers,
                                 ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, webProperties.getResources(), serverProperties.getError(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        super.setViewResolvers(viewResolvers.orderedStream().toList());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return route(all(), (request) -> this.renderErrorResponse(request, errorAttributes));
    }

    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request, ErrorAttributes errorAttributes) {
        final Throwable thread = errorAttributes.getError(request);
        if (thread instanceof final CustomException ce) {
            return ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(ResultContext.build(ce.getCode(), ce.getSketch(), ce.getContent())));
        }
        final Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        final Map<String, Object> result = new HashMap<>(6);
        result.put("path", error.get("path"));
        result.put("code", error.get("status"));
        result.put("message", error.get("error"));
        result.put("requestId", error.get("requestId"));
        final Object date = error.get("timestamp");
        if (date instanceof Date) {
            error.remove("timestamp");
            result.put("date", SIMPLE_DATE_FORMAT.format(date));
        }
        return ServerResponse
                .status(getHttpStatus(error))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(result));
    }

}

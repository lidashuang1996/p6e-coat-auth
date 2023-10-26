package club.p6e.coat.gateway.permission;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 权限过滤器
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = PermissionWebFilter.class,
        ignored = PermissionWebFilter.class
)
public class PermissionWebFilter implements WebFilter {

    /**
     * 格式化时间对象
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 权限验证器对象
     */
    private final PermissionValidator validator;

    /**
     * 构造方法初始化
     *
     * @param validator 权限验证器对象
     */
    public PermissionWebFilter(PermissionValidator validator) {
        this.validator = validator;
    }

    @Override
    public @NonNull Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final ServerHttpResponse response = exchange.getResponse();
        return validator
                .execute(request.getPath().value(), request.getMethod().name(), null)
                .map(r -> true)
                .switchIfEmpty(Mono.just(false))
                .flatMap(b -> {
                    if (b) {
                        return chain.filter(exchange);
                    } else {
                        final String result = "{\"timestamp\":\""
                                + DATE_TIME_FORMATTER.format(LocalDateTime.now())
                                + "\",\"path\":\""
                                + request.getPath()
                                + "\",\"message\":\"No Jurisdiction\",\"requestId\":\""
                                + request.getId() + "\",\"code\":401}";
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                        return response.writeWith(Mono.just(response.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8))));
                    }
                });
    }

}

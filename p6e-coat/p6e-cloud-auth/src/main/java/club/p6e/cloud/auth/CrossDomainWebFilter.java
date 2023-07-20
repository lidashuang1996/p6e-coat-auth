package club.p6e.cloud.auth;

import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * Cross Domain 过滤器
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class CrossDomainWebFilter implements WebFilter, Ordered {

    /**
     * 跨域配置 ACCESS_CONTROL_MAX_AGE
     */
    protected static final long ACCESS_CONTROL_MAX_AGE = 3600L;

    /**
     * 跨域配置 ACCESS_CONTROL_ALLOW_ORIGIN
     */
    protected static final String ACCESS_CONTROL_ALLOW_ORIGIN = "*";

    /**
     * 跨域配置 ACCESS_CONTROL_ALLOW_ORIGIN
     */
    protected static final boolean ACCESS_CONTROL_ALLOW_CREDENTIALS = true;

    /**
     * 跨域配置 ACCESS_CONTROL_ALLOW_HEADERS
     */
    protected static final String[] ACCESS_CONTROL_ALLOW_HEADERS = new String[]{
            "Authorization",
            "Content-Type",
            "Depth",
            "User-Agent",
            "X-File-Size",
            "X-Requested-With",
            "X-Requested-By",
            "If-Modified-Since",
            "X-File-Name",
            "X-File-Type",
            "Cache-Control",
            "Origin",
            "Client",
            "Cookie",
            "Referer"
    };

    /**
     * 跨域配置 ACCESS_CONTROL_ALLOW_METHODS
     */
    protected static final HttpMethod[] ACCESS_CONTROL_ALLOW_METHODS = new HttpMethod[]{
            HttpMethod.GET,
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.DELETE,
            HttpMethod.OPTIONS,
            HttpMethod.HEAD,
            HttpMethod.PATCH,
    };

    /**
     * 配置文件对象
     */
    protected final Properties properties;

    /**
     * 构造方法初始化
     *
     * @param properties 配置文件对象
     */
    public CrossDomainWebFilter(Properties properties) {
        this.properties = properties;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setAccessControlMaxAge(getAccessControlMaxAge());
        response.getHeaders().setAccessControlAllowHeaders(getAccessControlAllowHeaders());
        response.getHeaders().setAccessControlAllowMethods(getAccessControlAllowMethods());
        response.getHeaders().setAccessControlAllowOrigin(getAccessControlAllowOrigin(request));
        response.getHeaders().setAccessControlAllowCredentials(getAccessControlAllowCredentials());
        if (HttpMethod.OPTIONS.matches(request.getMethod().name().toUpperCase())) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        } else {
            return chain.filter(exchange);
        }
    }

    /**
     * 获取 AccessControlMaxAge
     *
     * @return AccessControlMaxAge
     */
    protected long getAccessControlMaxAge() {
        return ACCESS_CONTROL_MAX_AGE;
    }

    /**
     * 获取 AccessControlAllowCredentials
     *
     * @return AccessControlAllowCredentials
     */
    protected boolean getAccessControlAllowCredentials() {
        return ACCESS_CONTROL_ALLOW_CREDENTIALS;
    }

    /**
     * 获取 AccessControlAllowHeaders
     *
     * @return AccessControlAllowHeaders
     */
    protected List<String> getAccessControlAllowHeaders() {
        return Arrays.asList(ACCESS_CONTROL_ALLOW_HEADERS);
    }

    /**
     * 获取 AccessControlAllowMethods
     *
     * @return AccessControlAllowMethods
     */
    protected List<HttpMethod> getAccessControlAllowMethods() {
        return Arrays.asList(ACCESS_CONTROL_ALLOW_METHODS);
    }

    /**
     * 获取 AccessControlAllowOrigin
     *
     * @return AccessControlAllowOrigin
     */
    protected String getAccessControlAllowOrigin(ServerHttpRequest request) {
        final String origin = request.getHeaders().getOrigin();
        return origin == null ? ACCESS_CONTROL_ALLOW_ORIGIN : origin;
    }

}


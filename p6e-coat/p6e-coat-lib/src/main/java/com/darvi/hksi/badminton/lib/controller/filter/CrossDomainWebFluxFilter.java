package com.darvi.hksi.badminton.lib.controller.filter;

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

/**
 * 跨域过滤器
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class CrossDomainWebFluxFilter implements WebFilter {

    /**
     * 跨域配置 ACCESS_CONTROL_MAX_AGE
     */
    private static final long ACCESS_CONTROL_MAX_AGE = 3600L;

    /**
     * 跨域配置 ACCESS_CONTROL_ALLOW_ORIGIN
     */
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "*";

    /**
     * 跨域配置 ACCESS_CONTROL_ALLOW_ORIGIN
     */
    private static final boolean ACCESS_CONTROL_ALLOW_CREDENTIALS = true;

    /**
     * 跨域配置 ACCESS_CONTROL_ALLOW_HEADERS
     */
    private static final String[] ACCESS_CONTROL_ALLOW_HEADERS = new String[]{
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
            "Client"
    };

    /**
     * 跨域配置 ACCESS_CONTROL_ALLOW_METHODS
     */
    private static final HttpMethod[] ACCESS_CONTROL_ALLOW_METHODS = new HttpMethod[]{
            HttpMethod.GET,
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.DELETE,
            HttpMethod.OPTIONS,
    };

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setAccessControlMaxAge(ACCESS_CONTROL_MAX_AGE);
        response.getHeaders().setAccessControlAllowOrigin(ACCESS_CONTROL_ALLOW_ORIGIN);
        response.getHeaders().setAccessControlAllowCredentials(ACCESS_CONTROL_ALLOW_CREDENTIALS);
        response.getHeaders().setAccessControlAllowHeaders(Arrays.asList(ACCESS_CONTROL_ALLOW_HEADERS));
        response.getHeaders().setAccessControlAllowMethods(Arrays.asList(ACCESS_CONTROL_ALLOW_METHODS));
        // OPTIONS 请求直接返回成功
        if (HttpMethod.OPTIONS.matches(request.getMethodValue().toUpperCase())) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        } else {
            return chain.filter(exchange);
        }
    }
}

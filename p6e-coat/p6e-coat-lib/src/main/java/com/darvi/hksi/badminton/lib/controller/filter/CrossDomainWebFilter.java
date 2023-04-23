package com.darvi.hksi.badminton.lib.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.stream.Collectors;

/**
 * 浏览器跨域处理过滤器
 *
 * @author lidashuang
 * @version 1.0
 */
public class CrossDomainWebFilter implements Filter {

    /**
     * 注入日志系统
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CrossDomainWebFilter.class);

    /**
     * 分隔符号
     */
    private static final String ACCESS_CONTROL_DELIMITER = ",";

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

    /**
     * 初始化过滤器时候的方法回调
     *
     * @param filterConfig 过滤的配置对象
     */
    @Override
    public void init(FilterConfig filterConfig) {
        LOGGER.info("Filter [ CrossDomainWebFilter ] init complete ... ");
    }

    /**
     * 过滤器内容的处理方法
     *
     * @param servletRequest  服务请求头对象
     * @param servletResponse 服务返回头对象
     * @param filterChain     内部代理对象
     * @throws IOException      请求头和返回头处理时候可能出现的 IO 异常
     * @throws ServletException 服务处理时候可能出现的异常
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // 获取请求头和返回头
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 设置 Access-Control-Allow-Origin 内容为请求头 origin 的内容
        // 如果不存在 origin 的请求头，那么设置为 *
        String origin = ACCESS_CONTROL_ALLOW_ORIGIN;
        final Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                final String element = enumeration.nextElement();
                if ("origin".equalsIgnoreCase(element)) {
                    origin = request.getHeader(element);
                }
            }
        }

        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Max-Age", String.valueOf(ACCESS_CONTROL_MAX_AGE));
        response.setHeader("Access-Control-Allow-Credentials", String.valueOf(ACCESS_CONTROL_ALLOW_CREDENTIALS));
        response.setHeader("Access-Control-Allow-Headers", String.join(ACCESS_CONTROL_DELIMITER, ACCESS_CONTROL_ALLOW_HEADERS));
        response.setHeader("Access-Control-Allow-Methods",
                Arrays.stream(ACCESS_CONTROL_ALLOW_METHODS).map(HttpMethod::name).collect(Collectors.joining(ACCESS_CONTROL_DELIMITER)));

        // 是否为 OPTIONS 方法
        // 如果请求的方法为 options 那么将立即返回数据并设置返回头为 200
        // 关 HTTP OPTIONS 请求的唯一响应是 200 ，但是有一些情况，例如当内容长度为 0 时，一个 204 会更合适
        if (HttpMethod.OPTIONS.matches(request.getMethod().toUpperCase())) {
            // 返回 200 或者 204
            response.setStatus(200);
        } else {
            // 进入内部执行后续的方法
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 摧毁过滤器的时候的回调方法
     */
    @Override
    public void destroy() {
        LOGGER.info("Filter [ CrossDomainWebFilter ] destroy complete ... ");
    }

}

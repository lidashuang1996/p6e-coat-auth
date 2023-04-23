package com.darvi.hksi.badminton.lib.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 基础的 controller 类
 *
 * @author lidashuang
 * @version 1.0
 */
public class BaseWebController extends BaseController {

    /**
     * 获取 HttpServletRequest 对象
     *
     * @return HttpServletRequest 对象
     */
    public static HttpServletRequest getRequest() {
        final ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            return servletRequestAttributes.getRequest();
        }
        throw new RuntimeException(BaseWebController.class + " getRequest() HttpServletRequest error!");
    }

    /**
     * 获取 HttpServletResponse 对象
     *
     * @return HttpServletResponse 对象
     */
    public static HttpServletResponse getResponse() {
        final ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            return servletRequestAttributes.getResponse();
        }
        throw new RuntimeException(BaseWebController.class + " getResponse() HttpServletResponse error!");
    }

    /**
     * 获取 ACCESS TOKEN 内容
     *
     * @return ACCESS TOKEN 内容
     */
    protected String getAccessToken() {
        String accessToken = getParamToken(ACCESS_TOKEN_PARAM1, ACCESS_TOKEN_PARAM2);
        if (accessToken == null) {
            return getHeaderToken();
        } else {
            return accessToken;
        }
    }

    /**
     * 获取 REFRESH TOKEN 内容
     *
     * @return REFRESH TOKEN 内容
     */
    protected String getRefreshToken() {
        return getParamToken(REFRESH_TOKEN_PARAM1, REFRESH_TOKEN_PARAM2);
    }

    /**
     * 通过多个参数名称去获取 url 路径上面的参数值
     *
     * @param params 参数名称
     * @return 读取的参数名称对应的值
     */
    protected String getParamToken(String... params) {
        String value;
        final HttpServletRequest request = getRequest();
        for (String param : params) {
            value = request.getParameter(param);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取请求头部存在的 TOKEN 信息
     *
     * @return 头部的 TOKEN 信息
     */
    protected String getHeaderToken() {
        final HttpServletRequest request = getRequest();
        final String requestHeaderContent = request.getHeader(AUTH_HEADER);
        if (requestHeaderContent != null
                && requestHeaderContent.startsWith(AUTH_HEADER_TOKEN_PREFIX)) {
            return requestHeaderContent.substring(AUTH_HEADER_TOKEN_PREFIX.length());
        }
        return null;
    }
}

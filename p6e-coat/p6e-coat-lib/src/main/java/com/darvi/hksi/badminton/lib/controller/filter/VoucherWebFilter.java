package com.darvi.hksi.badminton.lib.controller.filter;

import com.darvi.hksi.badminton.lib.Properties;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import com.darvi.hksi.badminton.lib.context.ResultContext;
import com.darvi.hksi.badminton.lib.utils.JsonUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Enumeration;

/**
 * 凭证认证过滤器
 *
 * @author lidashuang
 * @version 1.0
 */
public class VoucherWebFilter implements Filter {

    /**
     * 凭证的头部
     */
    private static final String VOUCHER_HEADER = "HKSI-Badminton-Voucher";

    /**
     * 注入日志系统
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherWebFilter.class);

    /**
     * 初始化过滤器时候的方法回调
     *
     * @param config 过滤的配置对象
     */
    @Override
    public void init(FilterConfig config) {
        LOGGER.info("Filter [ VoucherFilter ] init complete ... ");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        final Properties properties = SpringUtil.getBean(Properties.class);
        final Properties.Security security = properties.getSecurity();
        if (security != null && security.isEnable()) {
            String voucher = null;
            final HttpServletRequest request = (HttpServletRequest) servletRequest;
            final Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                final String headerName = headerNames.nextElement();
                if (VOUCHER_HEADER.equalsIgnoreCase(headerName)) {
                    voucher = request.getHeader(headerName);
                    break;
                }
            }
            if (voucher != null) {
                for (final String v : security.getVouchers()) {
                    if (v.equalsIgnoreCase(voucher)) {
                        chain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                }
            }
            final HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            servletResponse.getWriter().write(
                    JsonUtil.toJson(
                            ResultContext.build(
                                    HttpStatus.UNAUTHORIZED.value(),
                                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                    HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    ));
            servletResponse.getWriter().flush();
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 摧毁过滤器的时候的回调方法
     */
    @Override
    public void destroy() {
        LOGGER.info("Filter [ VoucherFilter ] destroy complete ... ");
    }

}

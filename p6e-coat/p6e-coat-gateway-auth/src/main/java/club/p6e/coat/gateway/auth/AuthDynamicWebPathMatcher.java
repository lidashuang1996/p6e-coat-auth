package club.p6e.coat.gateway.auth;

import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

/**
 * 认证动态路径匹配接口
 *
 * @author lidashuang
 * @version 1.0
 */
public interface AuthDynamicWebPathMatcher extends ServerWebExchangeMatcher {

    /**
     * 添加需要认证的路径
     *
     * @param data 认证的路径
     */
    public void register(String data);

    /**
     * 移除需要认证的路径
     *
     * @param data 认证的路径
     */
    public void unregister(String data);

}

package club.p6e.coat.gateway.auth;

import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthDynamicWebPathMatcher extends ServerWebExchangeMatcher {

    public void register(String data);

    public void unregister(String data);

}

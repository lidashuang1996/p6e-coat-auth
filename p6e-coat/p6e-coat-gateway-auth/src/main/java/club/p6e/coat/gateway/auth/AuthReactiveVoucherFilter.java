package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.error.PathFormatException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthReactiveVoucherFilter implements WebFilter {

    /**
     * 需要拦截的认证的路径的缓存
     */
    private final List<PathPatternParserServerWebExchangeMatcher> list = new CopyOnWriteArrayList<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono
                .just(list)
                .flatMap(l -> aaa(exchange, 0, list))
                .flatMap(b -> {
                    if (b) {
                        final ServerHttpRequest request = exchange.getRequest();
                        final String voucher = request.getQueryParams().getFirst("voucher");
                        if (!StringUtils.hasText(voucher)) {
                            return Mono.error(new PathFormatException(this.getClass(), "", ""));
                        }
                        // 读取信息并写入
                    }
                    return chain.filter(exchange);
                });
    }

    public Mono<Boolean> aaa(ServerWebExchange exchange, int index,
                             List<PathPatternParserServerWebExchangeMatcher> list) {
        if (list.size() > 0 && index > 0 && index < list.size()) {
            final PathPatternParserServerWebExchangeMatcher matcher = list.get(index);
            return matcher
                    .matches(exchange)
                    .map(ServerWebExchangeMatcher.MatchResult::isMatch)
                    .flatMap(b -> b ? Mono.just(true) : aaa(exchange, index + 1, list));
        }
        return Mono.just(false);
    }

    public void register(PathPatternParserServerWebExchangeMatcher matcher) {
        list.add(matcher);
    }

    public void unregister(PathPatternParserServerWebExchangeMatcher matcher) {
        list.remove(matcher);
    }

}

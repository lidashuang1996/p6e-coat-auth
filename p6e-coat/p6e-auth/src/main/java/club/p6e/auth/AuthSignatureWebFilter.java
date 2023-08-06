package club.p6e.auth;

import club.p6e.auth.utils.AesUtil;
import jakarta.annotation.Nonnull;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 认证签名过滤器
 *
 * @author lidashuang
 * @version 1.0
 */
public class AuthSignatureWebFilter implements WebFilter, Ordered {

    private final Properties properties;

    public AuthSignatureWebFilter(Properties properties) {
        this.properties = properties;
    }

    @Override
    public int getOrder() {
        return -500;
    }

    @Override
    public @Nonnull Mono<Void> filter(ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
        final MultiValueMap<String, String> params = exchange.getRequest().getQueryParams();
        final String date = params.getFirst("date");
        final String signature = params.getFirst("signature");
        if (date != null && signature != null) {
            try {
                if (System.currentTimeMillis() - Long.parseLong(date) < 10000) {
                    return verification(exchange) ? chain.filter(exchange) : Mono.empty();
                }
            } catch (Exception e) {
                // ignore exceptions
            }
        }
        return Mono.empty();
    }

    private boolean verification(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        final MultiValueMap<String, String> params = request.getQueryParams();
        final String date = params.getFirst("date");
        final String signature = params.getFirst("signature");
        final String rPath = request.getPath().value();
        if (date != null && signature != null) {
            return signature.equals(AesUtil.decryption(
                    properties.getSignature().getSecret(),
                    rPath + "_" + date
            ));
        } else {
            return false;
        }
    }

}

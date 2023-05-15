package club.p6e.coat.gateway.auth;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthReactiveSignatureFilter extends AuthWebPathMatcherImpl implements WebFilter, AuthWebPathMatcher {
    private static final String SIGNATURE_URL_PARAM = "signature";
    private static final String DATE_URL_PARAM = "_";
    private AuthSignatureCodec codec;

    public AuthReactiveSignatureFilter(AuthSignatureCodec codec) {
        this.codec = codec;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final Map<String, String> map = new HashMap<>();
        final ServerHttpRequest request = exchange.getRequest();
        final MultiValueMap<String, String> params = request.getQueryParams();
        final String date = params.getFirst(DATE_URL_PARAM);
        final String signature = params.getFirst(SIGNATURE_URL_PARAM);
        if (!StringUtils.hasText(date)) {
            return Mono.error(new RuntimeException());
        }
        if (!StringUtils.hasText(signature)) {
            return Mono.error(new RuntimeException());
        }
        final List<String> keys = new ArrayList<>(params.keySet());
        keys.sort(Comparator.naturalOrder());
        keys.stream().filter(i -> !SIGNATURE_URL_PARAM.equals(i)).forEach(i -> map.put(i, params.getFirst(i)));
        return codec.encode(map).equals(signature) ? chain.filter(exchange) : Mono.error(new RuntimeException());
    }

}

package club.p6e.coat.gateway.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = AuthDynamicWebPathMatcher.class,
        ignored = AuthDynamicWebPathMatcherImpl.class
)
public class AuthDynamicWebPathMatcherImpl implements AuthDynamicWebPathMatcher {

    /**
     * 路径通配符
     */
    private static final String ADAPTER_CHAR = "*";

    private final List<String> list = new CopyOnWriteArrayList<>();

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        final List<PathContainer.Element> elements = request.getPath().elements();
        for (final String item : list) {
            boolean match = true;
            final String[] items = item.split("/");
            if (elements.size() < items.length) {
                break;
            } else {
                for (int i = 0; i < items.length; i++) {
                    if (items[i].equals(ADAPTER_CHAR + ADAPTER_CHAR)) {
                        break;
                    } else if (!items[i].equals(ADAPTER_CHAR)
                            && !items[i].equalsIgnoreCase(elements.get(i).value())) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return MatchResult.match();
                }
            }
        }
        return MatchResult.notMatch();
    }

    @Override
    public void register(String data) {
        if (data != null) {
            list.add(data);
        }
    }

    @Override
    public void unregister(String data) {
        if (data != null) {
            list.remove(data);
        }
    }
}



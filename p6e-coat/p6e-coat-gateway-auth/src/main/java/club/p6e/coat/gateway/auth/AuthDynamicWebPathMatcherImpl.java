package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.error.PathFormatException;
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

    /**
     * 路径连接符
     */
    private static final String PATH_CONNECT_CHAR = "/";

    /**
     * 需要拦截的认证的路径的缓存
     */
    private final List<String> list = new CopyOnWriteArrayList<>();

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        final List<PathContainer.Element> elements = request.getPath().elements();
        for (final String item : list) {
            boolean match = true;
            final String[] items = item.split(PATH_CONNECT_CHAR);
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
            final String[] ds = data.split(PATH_CONNECT_CHAR);
            final int length = ds.length;
            for (int i = 0; i < length; i++) {
                if (i + 1 != length && ds[i].equals(ADAPTER_CHAR + ADAPTER_CHAR)) {
                    throw new PathFormatException(this.getClass(), "", "");
                }
            }
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



package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.error.PathFormatException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = AuthGatewayWebPathMatcher.class,
        ignored = AuthGatewayWebPathMatcherImpl.class
)
public class AuthGatewayWebPathMatcherImpl implements AuthGatewayWebPathMatcher {

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
    private final List<String[]> list = new CopyOnWriteArrayList<>();
    private final Map<String, String[]> map = Collections.synchronizedMap(new HashMap<>());

    public AuthGatewayWebPathMatcherImpl() {
        register("/a/**");
    }

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        final List<PathContainer.Element> elements = request.getPath().elements();
        for (final String[] item : list) {
            boolean match = true;
            if (elements.size() < item.length) {
                break;
            } else {
                for (int i = 0; i < item.length; i++) {
                    if (item[i].equals(ADAPTER_CHAR + ADAPTER_CHAR)) {
                        break;
                    } else if (!item[i].equals(ADAPTER_CHAR)
                            && !item[i].equalsIgnoreCase(elements.get(i).value())) {
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
        if (data != null && data.length() > 0) {
            final List<String> l = new ArrayList<>();
            for (final String item : data.split(PATH_CONNECT_CHAR)) {
                if (item != null && item.length() > 0) {
                    l.add(PATH_CONNECT_CHAR);
                    l.add(item);
                }
            }
            final String[] ds = l.toArray(new String[0]);
            final int length = ds.length;
            for (int i = 0; i < length; i++) {
                if (i + 1 != length && ds[i].equals(ADAPTER_CHAR + ADAPTER_CHAR)) {
                    throw new PathFormatException(this.getClass(), "", "");
                }
            }
            list.add(ds);
            map.put(data, ds);
        }
    }

    @Override
    public void unregister(String data) {
        if (map.get(data) != null) {
            list.remove(map.remove(data));
        }
    }
}



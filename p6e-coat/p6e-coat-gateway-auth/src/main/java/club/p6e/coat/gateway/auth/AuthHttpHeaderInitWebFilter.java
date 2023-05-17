package club.p6e.coat.gateway.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.header.HttpHeaderWriterWebFilter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthHttpHeaderInitWebFilter extends HttpHeaderWriterWebFilter {

    /**
     * 清理的头部信息的前缀
     */
    private static String CLEANED_HEADER_NAME_PREFIX = "P6e-";

    /**
     * 设置清理的头部信息的前缀
     *
     * @param cleanedHeaderNamePrefix 清理的头名称前缀
     */
    public static void setCleanedHeaderNamePrefix(String cleanedHeaderNamePrefix) {
        CLEANED_HEADER_NAME_PREFIX = cleanedHeaderNamePrefix;
    }

    public AuthHttpHeaderInitWebFilter() {
        super(exchange -> {
            final ServerHttpRequest request = exchange.getRequest();
            exchange.mutate().request(request.mutate().headers(hh -> {
                final HttpHeaders headers = request.getHeaders();
                for (final String key : headers.keySet()) {
                    if (!key.startsWith(CLEANED_HEADER_NAME_PREFIX)) {
                        final List<String> value = headers.get(key);
                        if (value != null) {
                            hh.add(key, String.join(",", value));
                        }
                    }
                }
            }).build()).build();
            return Mono.empty();
        });
    }

}

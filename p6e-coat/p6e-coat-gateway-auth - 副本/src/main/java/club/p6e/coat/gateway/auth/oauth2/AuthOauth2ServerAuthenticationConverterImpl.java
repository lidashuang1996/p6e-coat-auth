package club.p6e.coat.gateway.auth.oauth2;

import club.p6e.coat.gateway.auth.AuthMarkAuthentication;
import club.p6e.coat.gateway.auth.AuthOauth2ServerAuthenticationConverter;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthOauth2ServerAuthenticationConverterImpl implements AuthOauth2ServerAuthenticationConverter {

    /**
     * 用户信息的头部名称
     */
    protected static final String USER_HEADER_NAME = "P6e-User-Info";

    /**
     * 认证头名称
     */
    protected static final String AUTH_HEADER = "Authorization";

    /**
     * 认证头类型
     */
    protected static final String AUTH_HEADER_TOKEN_TYPE = "Bearer";

    /**
     * 认证头必须的前缀
     */
    protected static final String AUTH_HEADER_TOKEN_PREFIX = AUTH_HEADER_TOKEN_TYPE + " ";

    /**
     * Query 方式 ACCESS TOKEN 参数 1
     */
    protected static final String ACCESS_TOKEN_PARAM1 = "accessToken";

    /**
     * Query 方式 ACCESS TOKEN 参数 2
     */
    protected static final String ACCESS_TOKEN_PARAM2 = "access_token";

    /**
     * 认证缓存的对象
     */
    protected final AuthCache cache;

    /**
     * 构造方法初始化
     *
     * @param cache 认证缓存的对象
     */
    public AuthOauth2ServerAuthenticationConverterImpl(AuthCache cache) {
        this.cache = cache;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String accessToken = null;
        final ServerHttpRequest request = exchange.getRequest();
        final List<String> authorizations = request.getHeaders().get(AUTH_HEADER);
        if (authorizations != null && authorizations.size() > 0) {
            final String authorization = authorizations.get(0);
            if (StringUtils.hasText(authorization)
                    && authorization.startsWith(AUTH_HEADER_TOKEN_PREFIX)) {
                accessToken = authorization.substring(AUTH_HEADER_TOKEN_PREFIX.length());
            }
        }
        if (accessToken == null) {
            accessToken = getParamValue(request, ACCESS_TOKEN_PARAM1, ACCESS_TOKEN_PARAM2);
        }
        if (accessToken == null) {
            return Mono.error(GlobalExceptionContext.exceptionAuthException(
                    this.getClass(),
                    "fun convert(ServerWebExchange exchange)",
                    "Auth exception. [not carrying certification identification]"
            ));
        } else {
            return cache
                    .getAccessToken(accessToken)
                    .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                            this.getClass(),
                            "fun convert(ServerWebExchange exchange)",
                            "Auth exception. [authentication information carried exception]")
                    ))
                    .flatMap(t -> cache
                            .getUser(t.getUid())
                            .map(u -> {
                                exchange.mutate().request(request.mutate().header(USER_HEADER_NAME, u).build()).build();
                                return new AuthMarkAuthentication(t.getUid(), u, List.of());
                            })
                    );
        }
    }

    /**
     * 通过多个参数名称去获取请求路径上面的参数值
     *
     * @param params 参数名称
     * @return 读取的参数名称对应的值
     */
    private String getParamValue(ServerHttpRequest request, String... params) {
        final MultiValueMap<String, String> qp = request.getQueryParams();
        for (final String key : params) {
            final List<String> values = qp.get(key);
            if (values != null && values.size() > 0) {
                return values.get(0);
            }
        }
        return null;
    }

}

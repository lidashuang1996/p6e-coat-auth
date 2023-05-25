//package club.p6e.coat.gateway.auth.certificate;
//
//import club.p6e.coat.gateway.auth.AuthServerAuthenticationConverter;
//import club.p6e.coat.gateway.auth.cache.AuthCache;
//import org.springframework.http.HttpCookie;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.security.authentication.TestingAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
///**
// * @author lidashuang
// * @version 1.0
// */
//public class AuthServerHttpCookieCacheAuthenticationConverter
//        extends AuthHttpCookieCertificate implements AuthServerAuthenticationConverter {
//
//    /**
//     * 用户信息的头部名称
//     */
//    protected static final String USER_HEADER_NAME = "P6e-User-Info";
//
//    /**
//     * 认证缓存的对象
//     */
//    protected final AuthCache cache;
//
//    /**
//     * 构造方法初始化
//     *
//     * @param cache 认证缓存的对象
//     */
//    public AuthServerHttpCookieCacheAuthenticationConverter(AuthCache cache) {
//        this.cache = cache;
//    }
//
//    @Override
//    public Mono<Authentication> convert(ServerWebExchange exchange) {
//        final ServerHttpRequest request = exchange.getRequest();
//        final List<HttpCookie> accessTokenCookies = getAccessTokenCookie(request);
//        if (accessTokenCookies == null || accessTokenCookies.size() == 0) {
//            return Mono.error(new RuntimeException());
//        } else {
//            final HttpCookie accessTokenCookie = accessTokenCookies.get(0);
//            final String accessToken = accessTokenCookie.getValue().trim();
//            return Mono
//                    .just(accessToken)
//                    .flatMap(cache::getAccessToken)
//                    .flatMap(t -> cache
//                            .getUser(t.getUid())
//                            .map(u -> {
//                                exchange.mutate().request(request.mutate().header(USER_HEADER_NAME, u).build()).build();
//                                return new TestingAuthenticationToken(t.getUid(), null, "");
//                            })
//                    );
//        }
//    }
//
//}

package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.AuthOAuth2User;
import club.p6e.coat.auth.AuthOAuth2Client;
import club.p6e.coat.auth.cache.OAuth2TokenClientAuthCache;
import club.p6e.coat.auth.cache.OAuth2TokenUserAuthCache;
import club.p6e.coat.auth.certificate.HttpCertificate;
import club.p6e.coat.auth.context.ResultContext;
import club.p6e.coat.common.utils.SpringUtil;
import club.p6e.coat.auth.error.GlobalExceptionContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class OAuth2InfoControllerImpl implements OAuth2InfoController<ResultContext> {

    /**
     * OAuth2TokenUserAuthCache
     */
    private final OAuth2TokenUserAuthCache userAuthCache;

    /**
     * OAuth2TokenClientAuthCache
     */
    private final OAuth2TokenClientAuthCache clientAuthCache;

    /**
     * 构造方法初始化
     *
     * @param userAuthCache   OAuth2TokenUserAuthCache
     * @param clientAuthCache OAuth2TokenClientAuthCache
     */
    public OAuth2InfoControllerImpl(OAuth2TokenUserAuthCache userAuthCache, OAuth2TokenClientAuthCache clientAuthCache) {
        this.userAuthCache = userAuthCache;
        this.clientAuthCache = clientAuthCache;
    }

    @Override
    public Mono<ResultContext> user(ServerWebExchange exchange) {
        return HttpCertificate
                .getHttpLocalStorageToken(exchange.getRequest())
                .flatMap(userAuthCache::getAccessToken)
                .flatMap(token -> userAuthCache.getUser(token.getUid()))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "fun user(ServerWebExchange exchange).",
                        "OAuth2 request access token does not exist."
                )))
                .flatMap(s -> {
                    try {
                        if (SpringUtil.exist(AuthOAuth2User.class)) {
                            return Mono.just(ResultContext.build(SpringUtil.getBean(AuthOAuth2User.class).create(s)));
                        } else {
                            return Mono.error(GlobalExceptionContext.exceptionBeanException(
                                    this.getClass(),
                                    "fun user(ServerWebExchange exchange).",
                                    "OAuth2 user info data bean [ " + AuthOAuth2User.class + " ] does not exist."
                            ));
                        }
                    } catch (Exception e) {
                        return Mono.error(GlobalExceptionContext.exceptionAuthException(
                                this.getClass(),
                                "fun user(ServerWebExchange exchange)",
                                "OAuth2 user info data analysis exception."
                        ));
                    }
                });
    }

    @Override
    public Mono<ResultContext> client(ServerWebExchange exchange) {
        return HttpCertificate
                .getHttpLocalStorageToken(exchange.getRequest())
                .flatMap(clientAuthCache::getAccessToken)
                .flatMap(token -> clientAuthCache.getClient(token.getCid()))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "fun user(ServerWebExchange exchange).",
                        "OAuth2 request access token does not exist."
                )))
                .flatMap(s -> {
                    try {
                        if (SpringUtil.exist(AuthOAuth2Client.class)) {
                            return Mono.just(ResultContext.build(SpringUtil.getBean(AuthOAuth2Client.class).create(s)));
                        } else {
                            return Mono.error(GlobalExceptionContext.exceptionBeanException(
                                    this.getClass(),
                                    "fun user(ServerWebExchange exchange).",
                                    "OAuth2 client info data bean [ " + AuthOAuth2Client.class + " ] does not exist."
                            ));
                        }
                    } catch (Exception e) {
                        return Mono.error(GlobalExceptionContext.exceptionAuthException(
                                this.getClass(),
                                "fun user(ServerWebExchange exchange).",
                                "OAuth2 client info data analysis exception."
                        ));
                    }
                });
    }

}

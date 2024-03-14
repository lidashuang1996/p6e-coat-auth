package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.AuthUser;
import club.p6e.coat.auth.cache.Oauth2TokenClientAuthCache;
import club.p6e.coat.auth.cache.Oauth2TokenUserAuthCache;
import club.p6e.coat.auth.certificate.HttpCertificate;
import club.p6e.coat.auth.context.ResultContext;
import club.p6e.coat.auth.utils.SpringUtil;
import club.p6e.coat.auth.error.GlobalExceptionContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2InfoControllerImpl implements Oauth2InfoController<ResultContext> {

    private final Oauth2TokenUserAuthCache userAuthCache;
    private final Oauth2TokenClientAuthCache clientAuthCache;

    public Oauth2InfoControllerImpl(Oauth2TokenUserAuthCache userAuthCache, Oauth2TokenClientAuthCache clientAuthCache) {
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
                        "fun user(ServerWebExchange exchange)",
                        "Oauth2 user info data authentication exception."
                )))
                .flatMap(s -> {
                    try {
                        if (SpringUtil.exist(AuthUser.class)) {
                            return Mono.just(ResultContext.build(SpringUtil.getBean(AuthUser.class).create(s)));
                        } else {
                            return Mono.error(GlobalExceptionContext.exceptionBeanException(
                                    this.getClass(),
                                    "fun user(ServerWebExchange exchange)",
                                    "Oauth2 user info data bean [ " + AuthUser.class + " ] exception."
                            ));
                        }
                    } catch (Exception e) {
                        return Mono.error(GlobalExceptionContext.exceptionAuthException(
                                this.getClass(),
                                "fun user(ServerWebExchange exchange)",
                                "Oauth2 user info data authentication exception."
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
                        "fun user(ServerWebExchange exchange)",
                        "Oauth2 client info data authentication exception."
                )))
                .flatMap(s -> {
                    try {
                        if (SpringUtil.exist(AuthUser.class)) {
                            return Mono.just(ResultContext.build(SpringUtil.getBean(AuthUser.class).create(s)));
                        } else {
                            return Mono.error(GlobalExceptionContext.exceptionBeanException(
                                    this.getClass(),
                                    "fun user(ServerWebExchange exchange)",
                                    "Oauth2 client info data bean [ " + AuthUser.class + " ] exception."
                            ));
                        }
                    } catch (Exception e) {
                        return Mono.error(GlobalExceptionContext.exceptionAuthException(
                                this.getClass(),
                                "fun user(ServerWebExchange exchange)",
                                "Oauth2 client info data authentication exception."
                        ));
                    }
                });
    }

}

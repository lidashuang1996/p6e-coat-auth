package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthCertificateValidator;
import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.cache.QrCodeLoginCache;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 二维码回调服务的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = QrCodeCallbackService.class,
//        ignored = QrCodeCallbackServiceDefaultImpl.class
//)
//@ConditionalOnExpression(QrCodeCallbackService.CONDITIONAL_EXPRESSION)
public class QrCodeCallbackServiceDefaultImpl implements QrCodeCallbackService {

    /**
     * 用户信息的请求头名称
     */
    private static final String USER_HEADER_NAME = "P6e-User-Info";

    /**
     * 二维码登录缓存
     */
    private final QrCodeLoginCache cache;
    private final AuthCertificateValidator interceptor;

    /**
     * 构造方法初始化
     *
     * @param cache 二维码登录缓存
     */
    public QrCodeCallbackServiceDefaultImpl(QrCodeLoginCache cache, AuthCertificateValidator interceptor) {
        this.cache = cache;
        this.interceptor = interceptor;
    }

    @Override
    public Mono<LoginContext.QrCodeCallback.Dto> execute(ServerWebExchange exchange, LoginContext.QrCodeCallback.Request param) {
        final String content = param.getContent();
        return interceptor
                .execute(exchange)
                .flatMap(e -> {
                    final ServerHttpRequest request = e.getRequest();
                    final HttpHeaders httpHeaders = request.getHeaders();
                    final List<String> list = httpHeaders.get(USER_HEADER_NAME);
                    if (list != null && list.size() > 0) {
                        return Mono.just(AuthUserDetails.create(list.get(0)));
                    } else {
                        return Mono.error(GlobalExceptionContext.exceptionAuthException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.QrCodeCallback.Request param)",
                                "QrCode callback login auth cache data does not exist or expire exception."
                        ));
                    }
                })
                .flatMap(u -> cache
                        .get(content)
                        .switchIfEmpty(Mono.error(GlobalExceptionContext.executeCacheException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.QrCodeCallback.Request param)",
                                "QrCode callback login cache data does not exist or expire exception."
                        )))
                        .flatMap(s -> {
                            if (QrCodeLoginCache.isEmpty(s)) {
                                return cache.set(content, String.valueOf(u.getId()));
                            } else {
                                return Mono.error(GlobalExceptionContext.executeCacheException(
                                        this.getClass(),
                                        "fun execute(ServerWebExchange exchange, LoginContext.QrCodeCallback.Request param)",
                                        "QrCode callback login cache other data exists exception."
                                ));
                            }
                        }))
                .flatMap(b -> {
                    if (b) {
                        return Mono.just(new LoginContext.QrCodeCallback.Dto().setContent("SUCCESS"));
                    } else {
                        return Mono.error(GlobalExceptionContext.executeCacheException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.QrCodeCallback.Request param)",
                                "QrCode callback login cache write exception."
                        ));
                    }
                });
    }
}

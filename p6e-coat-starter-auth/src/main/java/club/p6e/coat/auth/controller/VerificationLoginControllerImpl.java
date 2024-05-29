package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.AuthVoucher;
import club.p6e.coat.auth.certificate.HttpCertificate;
import club.p6e.coat.auth.AuthCertificateValidator;
import club.p6e.coat.auth.AuthUser;
import club.p6e.coat.auth.context.LoginContext;
import club.p6e.coat.common.context.ResultContext;
import club.p6e.coat.auth.error.GlobalExceptionContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;

/**
 * 验证登录的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class VerificationLoginControllerImpl
        implements VerificationLoginController<LoginContext.Verification.Request, ResultContext> {

    /**
     * 认证用户对象
     */
    private final AuthUser<?> au;

    /**
     * 认证证书拦截器对象
     */
    private final AuthCertificateValidator validator;

    /**
     * 构造方法初始化
     *
     * @param au        认证用户对象
     * @param validator 认证证书拦截器对象
     */
    public VerificationLoginControllerImpl(AuthUser<?> au, AuthCertificateValidator validator) {
        this.au = au;
        this.validator = validator;
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.Verification.Request param) {
        return validator
                .execute(exchange)
                .flatMap(e -> AuthVoucher
                        .init(e)
                        .flatMap(v -> {
                            final ServerHttpRequest request = e.getRequest();
                            final HttpHeaders httpHeaders = request.getHeaders();
                            final List<String> list = httpHeaders.get(HttpCertificate.getUserInfoHeaderName());
                            if (list != null && !list.isEmpty()) {
                                return au.create(list.get(0))
                                        .flatMap(u -> v.setOAuth2User(u.id(), u.serialize())
                                                .map(vv -> ResultContext.build(new HashMap<>() {{
                                                    if (v.isOAuth2()) {
                                                        put("oauth2", true);
                                                    }
                                                }}))
                                        );
                            } else {
                                return Mono.error(GlobalExceptionContext.exceptionAuthException(
                                        this.getClass(),
                                        "fun execute(ServerWebExchange exchange, LoginContext.Verification.Request param).",
                                        "Verification login authentication exception."
                                ));
                            }
                        }));
    }
}

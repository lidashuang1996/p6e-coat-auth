package club.p6e.auth.controller;

import club.p6e.auth.certificate.HttpCertificate;
import club.p6e.auth.AuthCertificateValidator;
import club.p6e.auth.AuthUser;
import club.p6e.auth.context.LoginContext;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.error.GlobalExceptionContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 验证登录的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class VerificationLoginControllerImpl
        implements VerificationLoginController<LoginContext.Verification.Request, ResultContext> {

    private final AuthUser<?> au;

    /**
     * 认证证书拦截器对象
     */
    private final AuthCertificateValidator validator;

    /**
     * 构造方法
     *
     * @param validator 验证器
     */
    public VerificationLoginControllerImpl(AuthUser<?> au, AuthCertificateValidator validator) {
        this.au = au;
        this.validator = validator;
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.Verification.Request param) {
        return validator
                .execute(exchange)
                .flatMap(e -> {
                    final ServerHttpRequest request = e.getRequest();
                    final HttpHeaders httpHeaders = request.getHeaders();
                    final List<String> list = httpHeaders.get(HttpCertificate.getUserHeaderName());
                    if (list != null && list.size() > 0) {
                        return Mono.just(ResultContext.build(au.create(list.get(0))));
                    } else {
                        return Mono.error(GlobalExceptionContext.exceptionAuthException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.Verification.Request param).",
                                "Verification login authentication exception."
                        ));
                    }
                });

    }
}

package club.p6e.auth.controller;

import club.p6e.auth.AuthCertificateAuthority;
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
     * 认证授权的服务对象
     */
    private final AuthCertificateAuthority authority;

    /**
     * 构造方法初始化
     *
     * @param au        认证用户对象
     * @param validator 认证证书拦截器对象
     */
    public VerificationLoginControllerImpl(AuthUser<?> au, AuthCertificateValidator validator, AuthCertificateAuthority authority) {
        this.au = au;
        this.validator = validator;
        this.authority = authority;
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.Verification.Request param) {
        return validator
                .execute(exchange)
                .flatMap(e -> {
                    final ServerHttpRequest request = e.getRequest();
                    final HttpHeaders httpHeaders = request.getHeaders();
                    final List<String> list = httpHeaders.get(HttpCertificate.getUserInfoHeaderName());
                    if (list != null && !list.isEmpty()) {
                        return authority
                                .award(exchange, au.create(list.get(0)))
                                .map(ResultContext::build);
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

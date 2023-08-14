package club.p6e.auth.controller;

import club.p6e.auth.AuthCertificateValidator;
import club.p6e.auth.AuthPage;
import club.p6e.auth.AuthUser;
import club.p6e.auth.certificate.HttpCertificate;
import club.p6e.auth.context.ResultContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author lidashuang
 * @version 1.0
 */
public class MeControllerImpl implements MeController<ResultContext> {

    /**
     * 认证用户对象
     */
    private final AuthUser<?> au;

    /**
     * 认证证书验证器对象
     */
    private final AuthCertificateValidator validator;

    /**
     * 构造方法初始化
     *
     * @param au        认证用户对象
     * @param validator 认证证书验证器对象
     */
    public MeControllerImpl(AuthUser<?> au, AuthCertificateValidator validator) {
        this.au = au;
        this.validator = validator;
    }

    @Override
    public Mono<Void> def(ServerWebExchange exchange) {
        final AuthPage.Model me = AuthPage.me();
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(me.getType());
        return response.writeWith(Mono.just(
                exchange.getResponse().bufferFactory().wrap(me.getContent().getBytes(StandardCharsets.UTF_8))
        ));
    }

    @Override
    public Mono<ResultContext> info(ServerWebExchange exchange) {
        return validator
                .execute(exchange)
                .map(e -> {
                    final String user = e
                            .getRequest()
                            .getHeaders()
                            .getFirst(HttpCertificate.getUserHeaderName());
                    return ResultContext.build(au.create(user));
                });
    }

}

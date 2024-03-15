package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.AuthCertificateValidator;
import club.p6e.coat.auth.AuthPage;
import club.p6e.coat.auth.AuthUser;
import club.p6e.coat.auth.AuthVoucher;
import club.p6e.coat.auth.certificate.HttpCertificate;
import club.p6e.coat.auth.context.ResultContext;
import club.p6e.coat.auth.utils.TemplateParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 我的信息接口的实现
 *
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
        return AuthVoucher.createMe().flatMap(v -> response.writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(TemplateParser.execute(
                        me.getContent(), "page", "me", "voucher", v.getMark()
                ).getBytes(StandardCharsets.UTF_8)))
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
                            .getFirst(HttpCertificate.getUserInfoHeaderName());
                    return ResultContext.build(au.create(user));
                });
    }

}

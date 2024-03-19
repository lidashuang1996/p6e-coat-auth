package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.AuthPage;
import club.p6e.coat.auth.AuthVoucher;
import club.p6e.coat.common.utils.TemplateParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 主页的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class IndexControllerImpl implements IndexController {

    @Override
    public Mono<Void> execute(ServerWebExchange exchange) {
        return AuthVoucher.createIndex().flatMap(v -> write(exchange, v.getMark()));
    }

    /**
     * 写入返回数据
     *
     * @param exchange ServerWebExchange 对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> write(ServerWebExchange exchange, String voucher) {
        final AuthPage.Model login = AuthPage.login();
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(login.getType());
        return response.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(
                TemplateParser.execute(
                        login.getContent(), "page", "login", "voucher", voucher
                ).getBytes(StandardCharsets.UTF_8)
        )));
    }

}

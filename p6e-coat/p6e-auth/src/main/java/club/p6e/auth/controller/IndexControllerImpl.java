package club.p6e.auth.controller;

import club.p6e.auth.AuthPage;
import club.p6e.auth.AuthVoucher;
import club.p6e.auth.utils.TemplateParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 主页的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class IndexControllerImpl implements IndexController {

    @Override
    public Mono<Void> execute(ServerWebExchange exchange) {
        final Map<String, String> m = new HashMap<>(2);
        m.put(AuthVoucher.INDEX, "true");
        m.put(AuthVoucher.INDEX_DATE, String.valueOf(System.currentTimeMillis()));
        return AuthVoucher
                .create(m)
                .flatMap(v -> write(exchange, v.getMark()));
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
                TemplateParser.execute(login.getContent(), "voucher", voucher).getBytes(StandardCharsets.UTF_8)
        )));
    }

}

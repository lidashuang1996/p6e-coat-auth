package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.utils.TemplateParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 首页
 *
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("")
public class IndexController {

    /**
     * 资源类型
     */
    private MediaType type;

    /**
     * 资源内容
     */
    private String content;

    @GetMapping("")
    public Mono<Void> def1(ServerWebExchange exchange) {
        return index(exchange);
    }

    @GetMapping("/")
    public Mono<Void> def2(ServerWebExchange exchange) {
        return index(exchange);
    }

    @GetMapping("/index")
    public Mono<Void> index(ServerWebExchange exchange) {
        final String c = content == null ? "@{voucher}" : content;
        final MediaType t = type == null ? MediaType.TEXT_PLAIN : type;
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(t);
        return AuthVoucherContext
                .create()
                .map(v -> TemplateParser.execute(c, "voucher", v.getMark()))
                .flatMap(r -> response.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(r.getBytes()))));
    }

    /**
     * 写入资源类型
     *
     * @param type 资源类型
     */
    public void setType(MediaType type) {
        this.type = type;
    }

    /**
     * 写入资源内容
     *
     * @param content 资源内容
     */
    public void setContent(String content) {
        this.content = content;
    }

}

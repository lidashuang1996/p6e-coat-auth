package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.utils.TemplateParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * IP 头部名称
     */
    @SuppressWarnings("ALL")
    private static final String IP_HEADER_NAME = "P6e-IP";

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
        final ServerHttpRequest request = exchange.getRequest();
        final ServerHttpResponse response = exchange.getResponse();
        final HttpHeaders headers = request.getHeaders();
        final List<String> ips = headers.get(IP_HEADER_NAME);
        String ip = "0.0.0.0";
        if (ips != null && ips.size() > 0) {
            ip = ips.get(0);
        }
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(t);
        final Map<String, String> map = new HashMap<>(2);
        map.put(AuthVoucherContext.IP, ip);
        map.put(AuthVoucherContext.INDEX, "true");
        map.put(AuthVoucherContext.INDEX_DATE, String.valueOf(System.currentTimeMillis()));
        return AuthVoucherContext
                .create(map)
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

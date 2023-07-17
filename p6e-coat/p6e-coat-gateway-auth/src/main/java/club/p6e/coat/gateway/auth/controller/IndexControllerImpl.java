package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.service.IndexService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 主页的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class IndexControllerImpl implements IndexController {

    /**
     * 主页的服务对象
     */
    private final IndexService service;

    /**
     * 构造方法初始化
     *
     * @param service 主页的服务对象
     */
    public IndexControllerImpl(IndexService service) {
        this.service = service;
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange) {
        final Map<String, String> m = new HashMap<>();
        m.put(AuthVoucher.INDEX, "true");
        m.put(AuthVoucher.INDEX_DATE, String.valueOf(System.currentTimeMillis()));
        return service.execute(exchange, m);
    }

}

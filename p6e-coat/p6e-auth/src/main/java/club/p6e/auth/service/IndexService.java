package club.p6e.auth.service;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 主页的控制器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface IndexService {

    /**
     * 执行首页内容
     *
     * @param exchange ServerWebExchange 对象
     * @param vm       凭证初始化缓存的对象
     * @return Mono<Void> 无返回
     */
    public Mono<Void> execute(ServerWebExchange exchange, Map<String, String> vm);

}

package club.p6e.auth.service;

import club.p6e.auth.context.RegisterContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 注册服务
 *
 * @author lidashuang
 * @version 1.0
 */
public interface RegisterService {

    /**
     * 注册服务执行
     *
     * @param exchange ServerWebExchange 对象
     * @param param    请求对象
     * @return 结果对象
     */
    public Mono<RegisterContext.Dto> execute(ServerWebExchange exchange, RegisterContext.Request param);

}

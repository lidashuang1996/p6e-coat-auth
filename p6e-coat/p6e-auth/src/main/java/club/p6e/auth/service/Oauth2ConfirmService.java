package club.p6e.auth.service;

import club.p6e.auth.context.Oauth2Context;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 二次认证确认的服务
 *
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2ConfirmService {

    /**
     * 执行客户端刷新令牌服务
     *
     * @param request 请求对象
     * @return 结果对象
     */
    public Mono<Oauth2Context.Confirm.Dto> execute(ServerWebExchange exchange, Oauth2Context.Confirm.Request request);

}

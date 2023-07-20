package club.p6e.cloud.auth.service;

import club.p6e.cloud.auth.context.Oauth2Context;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 TOKEN
 *
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2TokenService {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION = "#{${p6e.auth.oauth2.enable:false}}";

    public Mono<Oauth2Context.Token.Dto> execute(ServerWebExchange exchange, Oauth2Context.Token.Request param);

}

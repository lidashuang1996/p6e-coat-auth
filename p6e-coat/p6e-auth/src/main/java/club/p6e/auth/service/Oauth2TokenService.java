package club.p6e.auth.service;

import club.p6e.auth.context.OAuth2Context;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 TOKEN
 *
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2TokenService {

    public Mono<OAuth2Context.Token.Dto> execute(ServerWebExchange exchange, OAuth2Context.Token.Request param);

}

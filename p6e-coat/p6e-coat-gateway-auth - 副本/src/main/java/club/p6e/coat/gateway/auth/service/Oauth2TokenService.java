package club.p6e.coat.gateway.auth.service;

import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2TokenService {


    Mono<Object> grantTypePassword();

    Mono<Object> grantTypeClientCredentials();

    Mono<Object> grantTypeRefreshToken();

    Mono<Object> grantTypeAuthorizationCode();
}

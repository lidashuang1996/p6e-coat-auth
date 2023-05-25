package club.p6e.coat.gateway.auth;

import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthCertificateAuthority {
    public Mono<Object> issue(AuthUser user);

}

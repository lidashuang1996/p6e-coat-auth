package club.p6e.coat.gateway.auth;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthTokenCacheCertificateAuthority implements AuthCertificateAuthority {

    @Override
    public Mono<Object> issue(AuthUser user) {
        return Mono.just(user.toMap());
    }

}

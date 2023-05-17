package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthHttpCookieRedisCacheCertificate
        extends AuthHttpCookieCertificate implements AuthCertificate {

    @Override
    public Object use(ServerWebExchange exchange, AuthUserDetails authUserDetails) {
        return null;
    }

}

package club.p6e.coat.gateway;

import club.p6e.coat.gateway.auth.AuthJsonWebTokenCipher;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class B extends AuthJsonWebTokenCipher {

    @Override
    public String getAccessTokenSecret() {
        return "77777777777777777777777777777777";
    }

    @Override
    public String getRefreshTokenSecret() {
        return "88888888888888888888888888888888";
    }
}

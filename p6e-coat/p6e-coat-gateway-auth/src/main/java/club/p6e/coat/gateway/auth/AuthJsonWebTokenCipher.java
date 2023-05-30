package club.p6e.coat.gateway.auth;

import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthJsonWebTokenCipher {

    /**
     * 获取 ACCESS TOKEN 密钥
     *
     * @return ACCESS TOKEN 密钥
     */
    public String getAccessTokenSecret() {
        return "123";
    }

    /**
     * 获取 REFRESH TOKEN 密钥
     *
     * @return REFRESH TOKEN 密钥
     */
    public String getRefreshTokenSecret() {
        return "4444";
    }

}

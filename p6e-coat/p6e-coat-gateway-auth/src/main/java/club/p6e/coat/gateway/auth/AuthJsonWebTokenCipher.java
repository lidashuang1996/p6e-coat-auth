package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.utils.GeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthJsonWebTokenCipher {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthJsonWebTokenCipher.class);

    private String accessTokenSecret;
    private String refreshTokenSecret;

    public void init() {
        if (this.accessTokenSecret == null) {
            this.accessTokenSecret = DigestUtils.md5DigestAsHex(
                    ("AS_" + GeneratorUtil.uuid() + GeneratorUtil.random()).getBytes(StandardCharsets.UTF_8)
            );
            LOGGER.info("[ JWT ( ACCESS_TOKEN ) ] INIT >>> " + this.accessTokenSecret);
        }
        if (this.refreshTokenSecret == null) {
            this.refreshTokenSecret = DigestUtils.md5DigestAsHex(
                    ("RS_" + GeneratorUtil.uuid() + GeneratorUtil.random()).getBytes(StandardCharsets.UTF_8)
            );
            LOGGER.info("[ JWT ( REFRESH_TOKEN ) ] INIT >>> " + this.refreshTokenSecret);
        }
    }

    /**
     * 获取 ACCESS TOKEN 密钥
     *
     * @return ACCESS TOKEN 密钥
     */
    public String getAccessTokenSecret() {
        init();
        return accessTokenSecret;
    }

    /**
     * 获取 REFRESH TOKEN 密钥
     *
     * @return REFRESH TOKEN 密钥
     */
    public String getRefreshTokenSecret() {
        init();
        return refreshTokenSecret;
    }

    public void setAccessTokenSecret(String secret) {
        this.accessTokenSecret = secret;
        LOGGER.info(this.accessTokenSecret);
    }

    public void setRefreshTokenSecret(String secret) {
        this.refreshTokenSecret = secret;
        LOGGER.info(this.refreshTokenSecret);
    }
}

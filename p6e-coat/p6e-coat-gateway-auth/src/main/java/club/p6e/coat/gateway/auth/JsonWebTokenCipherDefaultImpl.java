package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.utils.GeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = JsonWebTokenCipher.class,
        ignored = JsonWebTokenCipherDefaultImpl.class
)
public class JsonWebTokenCipherDefaultImpl implements JsonWebTokenCipher {

    /**
     * 注入日志系统
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWebTokenCipherDefaultImpl.class);

    /**
     * 设置的 ACCESS TOKEN 密钥
     */
    private String accessTokenSecret;

    /**
     * 设置的 REFRESH TOKEN 密钥
     */
    private String refreshTokenSecret;

    /**
     * 默认的密钥
     */
    private final String defaultSecret;

    /**
     * 构造方法初始化
     */
    public JsonWebTokenCipherDefaultImpl() {
        this.defaultSecret = GeneratorUtil.random(32, true, false);
        this.accessTokenSecret = defaultSecret;
        this.refreshTokenSecret = defaultSecret;
        LOGGER.info("[ P6E AUTH JSON WEB TOKEN ] DEFAULT SECRET ==> " + defaultSecret);
    }

    @Override
    public String getAccessTokenSecret() {
        return accessTokenSecret == null || accessTokenSecret.isEmpty() ? defaultSecret : accessTokenSecret;
    }

    @Override
    public String getRefreshTokenSecret() {
        return refreshTokenSecret == null || refreshTokenSecret.isEmpty() ? defaultSecret : refreshTokenSecret;
    }

    public void setAccessTokenSecret(String secret) {
        this.accessTokenSecret = secret;
    }

    public void setRefreshTokenSecret(String secret) {
        this.refreshTokenSecret = secret;
    }

}

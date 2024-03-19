package club.p6e.coat.auth.client;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Component("club.p6e.coat.auth.client.Properties")
@ConfigurationProperties(prefix = "club.p6e.coat.auth.client")
public class Properties implements Serializable {

    /**
     * AUTHORIZE URL
     */
    private String authorizeUrl;

    /**
     * AUTHORIZE TOKEN URL
     */
    private String authorizeTokenUrl;

    /**
     * AUTHORIZE USER INFO URL
     */
    private String authorizeUserInfoUrl;

    /**
     * AUTHORIZE APP ID
     */
    private String authorizeAppId;

    /**
     * AUTHORIZE APP SECRET
     */
    private String authorizeAppSecret;

    /**
     * APP REDIRECT URI
     */
    private String authorizeAppRedirectUri;

    /**
     * JWT ACCESS TOKEN SECRET
     */
    private String jwtAccessTokenSecret;

    /**
     * JWT REFRESH TOKEN SECRET
     */
    private String jwtRefreshTokenSecret;

    /**
     * 缓存类型
     */
    private Cache cache = new Cache();

    @Data
    @Accessors(chain = true)
    public static class Cache implements Serializable {
        /**
         * 缓存方式的配置
         */
        private Type type = Type.REDIS;

        /**
         * 缓存枚举类型
         */
        public enum Type implements Serializable {
            REDIS,
            MEMORY
        }
    }

}

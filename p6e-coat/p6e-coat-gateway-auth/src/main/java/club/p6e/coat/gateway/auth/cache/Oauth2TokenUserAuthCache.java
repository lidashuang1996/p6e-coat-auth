package club.p6e.coat.gateway.auth.cache;

import lombok.Data;
import lombok.experimental.Accessors;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Optional;

/**
 * OAUTH2 用户令牌信息缓存
 *
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2TokenUserAuthCache {

    /**
     * 令牌模型
     */
    @Data
    @Accessors(chain = true)
    public static class Token implements Serializable {

        /**
         * UID
         */
        private String uid;

        /**
         * 作用域
         */
        private String scope;

        /**
         * ACCESS TOKEN1
         */
        private String accessToken;

        /**
         * REFRESH TOKEN
         */
        private String refreshToken;

    }

    /**
     * 分割符号
     */
    public static final String DELIMITER = ":";

    /**
     * 过期的时间
     */
    public static final long EXPIRATION_TIME = 60 * 15;

    /**
     * 用户缓存前缀
     */
    public static final String USER_PREFIX = "OAUTH2:USER_AUTH_TOKEN:USER:";

    /**
     * ACCESS TOKEN 缓存前缀
     */
    public static final String ACCESS_TOKEN_PREFIX = "OAUTH2:USER_AUTH_TOKEN:ACCESS_TOKEN:";

    /**
     * REFRESH TOKEN 缓存前缀
     */
    public static final String REFRESH_TOKEN_PREFIX = "OAUTH2:USER_AUTH_TOKEN:REFRESH_TOKEN:";

    /**
     * 用户 ACCESS TOKEN 缓存前缀
     */
    public static final String USER_ACCESS_TOKEN_PREFIX = "OAUTH2:USER_AUTH_TOKEN:USER:ACCESS_TOKEN:";

    /**
     * 用户 REFRESH TOKEN 缓存前缀
     */
    public static final String USER_REFRESH_TOKEN_PREFIX = "OAUTH2:USER_AUTH_TOKEN:USER:REFRESH_TOKEN:";

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION = "#{${p6e.auth.oauth2.enable:false}}";

    /**
     * @param uid   用户ID
     * @param user  用户信息
     * @param scope 用户的作用域
     * @return 令牌对象
     */
    public Mono<Token> set(String uid, String user, String scope, String accessToken, String refreshToken);

    /**
     * 读取信息
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    public Optional<String> get(String uid);

    /**
     * 读取令牌
     *
     * @param token 令牌
     * @return 令牌对象
     */
    public Optional<Token> getAccessToken(String token);

    /**
     * 读取刷新令牌
     *
     * @param token 刷新令牌
     * @return 令牌对象
     */
    public Optional<Token> getRefreshToken(String token);

    /**
     * 清除令牌
     *
     * @param token 令牌
     */
    public void cleanToken(String token);

}

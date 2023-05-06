package club.p6e.coat.gateway.auth.cache;

import club.p6e.coat.gateway.auth.cache.support.ICache;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Optional;

/**
 * 认证缓存服务
 *
 * @author lidashuang
 * @version 1.0
 */
public interface AuthCache extends ICache {

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
    public static final long EXPIRATION_TIME = 60 * 60 * 24;

    /**
     * 用户缓存前缀
     */
    public static final String USER_PREFIX = "AUTH:USER:";

    /**
     * 用户令牌缓存前缀
     */
    public static final String USER_TOKEN_PREFIX = "AUTH:USER:TOKEN:";

    /**
     * ACCESS TOKEN 缓存前缀
     */
    public static final String ACCESS_TOKEN_PREFIX = "AUTH:ACCESS_TOKEN:";

    /**
     * REFRESH TOKEN 缓存前缀
     */
    public static final String REFRESH_TOKEN_PREFIX = "AUTH:REFRESH_TOKEN:";

    /**
     * 条件注册的条件表达式
     */
    public static final String CONDITIONAL_EXPRESSION = "#{${p6e.auth.login.enable:false}}";

    /**
     * 写入认证数据
     *
     * @param uid  用户 ID 数据
     * @param user 写入用户信息数据
     * @return 令牌对象
     */
    public Token set(String uid, String user, String accessToken, String refreshToken);

    /**
     * 通过用户 ID 获取数据
     *
     * @param uid 用户 ID 数据
     * @return 用户信息字符串
     */
    public Optional<String> get(String uid);

    /**
     * 通过 Access Token 获取数据
     *
     * @param token Access Token 数据
     * @return 令牌对象
     */
    public Optional<Token> getAccessToken(String token);

    /**
     * 通过 Refresh Token 获取数据
     *
     * @param token Refresh Token 数据
     * @return 令牌对象
     */
    public Optional<Token> getRefreshToken(String token);

    /**
     * 清除用户的 Access Token 以及 Refresh Token 数据
     *
     * @param token Access Token 数据
     */
    public void cleanToken(String token);

    /**
     * 清除用户的全部信息
     *
     * @param token Access Token 数据
     */
    public void cleanTokenAll(String token);

}

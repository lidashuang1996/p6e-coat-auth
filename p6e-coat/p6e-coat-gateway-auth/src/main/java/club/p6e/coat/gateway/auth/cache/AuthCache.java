package club.p6e.coat.gateway.auth.cache;

import club.p6e.coat.gateway.auth.cache.support.ICache;
import lombok.Data;
import lombok.experimental.Accessors;
import reactor.core.publisher.Mono;

import java.io.Serializable;

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
    class Token implements Serializable {

        /**
         * UID
         */
        private String uid;

        private String device;

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
    String DELIMITER = ":";

    /**
     * 过期的时间
     */
    long EXPIRATION_TIME = 60 * 60 * 24;

    /**
     * 用户缓存前缀
     */
    String USER_PREFIX = "AUTH:USER:";

    /**
     * 用户令牌缓存前缀
     */
    String USER_TOKEN_LIST_PREFIX = "AUTH:USER:TOKEN:";

    /**
     * ACCESS TOKEN 缓存前缀
     */
    String ACCESS_TOKEN_PREFIX = "AUTH:ACCESS_TOKEN:";

    /**
     * REFRESH TOKEN 缓存前缀
     */
    String REFRESH_TOKEN_PREFIX = "AUTH:REFRESH_TOKEN:";

    /**
     * 写入认证数据
     *
     * @param uid          用户 ID 数据
     * @param accessToken  ACCESS TOKEN 数据
     * @param refreshToken REFRESH TOKEN 数据
     * @param user         写入用户信息数据
     * @return 令牌对象
     */
    Mono<Token> set(String uid, String device, String accessToken, String refreshToken, String user);

    /**
     * 通过用户 ID 获取数据
     *
     * @param uid 用户 ID 数据
     * @return 用户信息字符串
     */
    Mono<String> get(String uid);

    /**
     * 通过 Access Token 获取数据
     *
     * @param token Access Token 数据
     * @return 令牌对象
     */
    Mono<Token> getAccessToken(String content);

    /**
     * 通过 Refresh Token 获取数据
     *
     * @param token Refresh Token 数据
     * @return 令牌对象
     */
    Mono<Token> getRefreshToken(String content);

    /**
     * 清除用户的 Access Token 以及 Refresh Token 数据
     *
     * @param token Access Token 数据
     */
    Mono<Long> cleanToken(String content);

    Mono<Long> cleaUserAll(String uid);

    /**
     * 清除用户的全部信息
     *
     * @param token Access Token 数据
     */
    Mono<Long> cleanTokenAll(String content);

}

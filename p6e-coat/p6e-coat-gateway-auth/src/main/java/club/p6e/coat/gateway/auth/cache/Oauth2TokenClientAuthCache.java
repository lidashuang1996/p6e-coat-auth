package club.p6e.coat.gateway.auth.cache;

import lombok.Data;
import lombok.experimental.Accessors;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Optional;

/**
 * OAUTH2 客户端令牌信息缓存
 *
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2TokenClientAuthCache {

    /**
     * 令牌模型
     */
    @Data
    @Accessors(chain = true)
    public static class Token implements Serializable {

        /**
         * CID
         */
        private String cid;

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
     * 客户端缓存前缀
     */
    public static final String CLIENT_PREFIX = "OAUTH2:CLIENT_AUTH_TOKEN:CLIENT:";

    /**
     * ACCESS TOKEN 缓存前缀
     */
    public static final String ACCESS_TOKEN_PREFIX = "OAUTH2:CLIENT_AUTH_TOKEN:ACCESS_TOKEN:";

    /**
     * REFRESH TOKEN 缓存前缀
     */
    public static final String REFRESH_TOKEN_PREFIX = "OAUTH2:CLIENT_AUTH_TOKEN:REFRESH_TOKEN:";

    /**
     * 客户端 ACCESS TOKEN 缓存前缀
     */
    public static final String CLIENT_TOKEN_LIST_PREFIX = "OAUTH2:CLIENT_AUTH_TOKEN:LIST:";

    /**
     * 客户端 REFRESH TOKEN 缓存前缀
     */
    public static final String CLIENT_REFRESH_TOKEN_PREFIX = "OAUTH2:CLIENT_AUTH_TOKEN:CLIENT:REFRESH_TOKEN:";

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION = "#{${p6e.auth.oauth2.enable:false}}";

    /**
     * @param cid   客户端ID
     * @param client  客户端信息
     * @param scope 客户端的作用域
     * @return 令牌对象
     */
    public Mono<Token> set(String cid, String client, String scope, String accessToken, String refreshToken);

    /**
     * 读取信息
     *
     * @param cid 客户端ID
     * @return 客户端信息
     */
    public Mono<String> getClient(String cid);

    /**
     * 读取令牌
     *
     * @param token 令牌
     * @return 令牌对象
     */
    public Mono<Token> getAccessToken(String token);

    /**
     * 读取刷新令牌
     *
     * @param token 刷新令牌
     * @return 令牌对象
     */
    public Mono<Token> getRefreshToken(String token);

    /**
     * 清除令牌
     *
     * @param token 令牌
     */
    public Mono<Long> cleanToken(String token);

}

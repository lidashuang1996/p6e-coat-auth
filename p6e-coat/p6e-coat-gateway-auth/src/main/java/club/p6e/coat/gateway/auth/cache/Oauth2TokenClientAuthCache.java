package club.p6e.coat.gateway.auth.cache;

import lombok.Data;
import lombok.experimental.Accessors;
import reactor.core.publisher.Mono;

import java.io.Serializable;

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
    class Token implements Serializable {

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
     * 过期的时间
     */
    long EXPIRATION_TIME = 900L;

    /**
     * 客户端缓存前缀
     */
    String CLIENT_PREFIX = "OAUTH2:CLIENT_AUTH_TOKEN:CLIENT:";

    /**
     * ACCESS TOKEN 缓存前缀
     */
    String ACCESS_TOKEN_PREFIX = "OAUTH2:CLIENT_AUTH_TOKEN:ACCESS_TOKEN:";

    /**
     * REFRESH TOKEN 缓存前缀
     */
    String REFRESH_TOKEN_PREFIX = "OAUTH2:CLIENT_AUTH_TOKEN:REFRESH_TOKEN:";

    /**
     * 客户端 ACCESS TOKEN 缓存前缀
     */
    String CLIENT_TOKEN_LIST_PREFIX = "OAUTH2:CLIENT_AUTH_TOKEN:TOKEN_LIST:";

    /**
     * 条件注册的条件表达式
     */
    String CONDITIONAL_EXPRESSION = "#{${p6e.auth.oauth2.enable:false}}";

    /**
     * @param cid          客户端ID
     * @param scope        客户端的作用域
     * @param accessToken  Access Token
     * @param refreshToken Refresh Token
     * @param client       客户端信息
     * @return 令牌对象
     */
    Mono<Token> set(String cid, String scope, String accessToken, String refreshToken, String client);

    /**
     * 读取信息
     *
     * @param cid 客户端编号
     * @return 客户端信息
     */
    Mono<String> getClient(String cid);

    /**
     * 读取令牌
     *
     * @param token 令牌
     * @return 令牌对象
     */
    Mono<Token> getAccessToken(String token);

    /**
     * 读取刷新令牌
     *
     * @param token 刷新令牌
     * @return 令牌对象
     */
    Mono<Token> getRefreshToken(String token);

    /**
     * 清除令牌
     *
     * @param token 令牌
     * @return 清除的令牌条数
     */
    Mono<Long> cleanToken(String token);

}

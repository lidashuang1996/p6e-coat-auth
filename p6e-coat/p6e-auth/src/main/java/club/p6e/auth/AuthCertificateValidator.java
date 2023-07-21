package club.p6e.auth;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证证书验证器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface AuthCertificateValidator {

    /**
     * 认证令牌的验证
     *
     * @param token 令牌
     * @return 读取的令牌里面的认证信息
     */
    public Mono<String> accessToken(String token);

    /**
     * 刷新令牌的验证
     *
     * @param token 令牌
     * @return 读取的令牌里面的认证信息
     */
    public Mono<String> refreshToken(String token);

    /**
     * 执行认证方法
     *
     * @param exchange ServerWebExchange 对象
     * @return ServerWebExchange 对象
     */
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange);

}

package club.p6e.auth;

import club.p6e.auth.context.ResultContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证证书下发器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface AuthCertificateAuthority {

    /**
     * 执行证书颁发
     *
     * @param exchange ServerWebExchange 对象
     * @param model    用户认证模型
     * @return 读取的令牌里面的认证信息
     */
    public Mono<ResultContext> award(ServerWebExchange exchange, AuthUser.Model model);

    /**
     * 执行证书废除
     *
     * @param exchange ServerWebExchange 对象
     * @return 空的返回
     */
    public Mono<Void> abolish(ServerWebExchange exchange);

}

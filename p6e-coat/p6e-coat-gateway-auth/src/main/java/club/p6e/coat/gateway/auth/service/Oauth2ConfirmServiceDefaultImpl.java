package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.cache.Oauth2CodeCache;
import club.p6e.coat.gateway.auth.context.Oauth2Context;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.generator.Oauth2CodeGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * OAUTH2 二次认证确认的服务的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = Oauth2ConfirmService.class,
//        ignored = Oauth2ConfirmServiceDefaultImpl.class
//)
//@ConditionalOnExpression(Oauth2ConfirmService.CONDITIONAL_EXPRESSION)
public class Oauth2ConfirmServiceDefaultImpl implements Oauth2ConfirmService {

    /**
     * Oauth2 Code 缓存对象
     */
    private final Oauth2CodeCache cache;

    /**
     * Oauth2 Code 生成器对象
     */
    private final Oauth2CodeGenerator generator;

    /**
     * 构造方法初始化
     *
     * @param cache     Oauth2 Code 缓存对象
     * @param generator Oauth2 Code 生成器对象
     */
    public Oauth2ConfirmServiceDefaultImpl(
            Oauth2CodeCache cache, Oauth2CodeGenerator generator) {
        this.cache = cache;
        this.generator = generator;
    }

    @Override
    public Mono<Oauth2Context.Confirm.Dto> execute(ServerWebExchange exchange, Oauth2Context.Confirm.Request param) {
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> {
                    final String oauth2 = v.get(AuthVoucher.OAUTH2);
                    final String state = v.get(AuthVoucher.OAUTH2_STATE);
                    final String scope = v.get(AuthVoucher.OAUTH2_SCOPE);
                    final String clientId = v.get(AuthVoucher.OAUTH2_CLIENT_ID);
                    final String redirectUri = v.get(AuthVoucher.OAUTH2_REDIRECT_URI);
                    final String userId = v.get(AuthVoucher.OAUTH2_USER_ID);
                    final String userInfo = v.get(AuthVoucher.OAUTH2_USER_INFO);
                    if (oauth2 == null) {
                        return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(), "fun execute(Oauth2Context.Auth.Request param).", ""));
                    }
                    if (scope == null) {
                        return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(), "fun execute(Oauth2Context.Auth.Request param).", ""));
                    }
                    if (clientId == null) {
                        return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(), "fun execute(Oauth2Context.Auth.Request param).", ""));
                    }
                    if (redirectUri == null) {
                        return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(), "fun execute(Oauth2Context.Auth.Request param).", ""));
                    }
                    if (userId == null) {
                        return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(), "fun execute(Oauth2Context.Auth.Request param).", ""));
                    }
                    if (userInfo == null) {
                        return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(), "fun execute(Oauth2Context.Auth.Request param).", ""));
                    }
                    final String code = generator.execute();
                    final Map<String, String> map = new HashMap<>(5);
                    map.put(Oauth2CodeCache.OAUTH2_SCOPE, scope);
                    map.put(Oauth2CodeCache.OAUTH2_CLIENT_ID, clientId);
                    map.put(Oauth2CodeCache.OAUTH2_USER_ID, userId);
                    map.put(Oauth2CodeCache.OAUTH2_USER_INFO, userInfo);
                    map.put(Oauth2CodeCache.OAUTH2_REDIRECT_URI, redirectUri);
                    return cache
                            .set(code, map)
                            .map(b -> new Oauth2Context.Confirm.Dto()
                                    .setCode(code)
                                    .setState(state)
                                    .setRedirectUri(redirectUri));
                });
    }

}

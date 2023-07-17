package club.p6e.coat.gateway.auth.validator;

import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.validator.ParameterValidatorInterface;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeLoginCallbackParameterValidator implements ParameterValidatorInterface {

    /**
     * 执行顺序
     */
    private static final int ORDER = 0;

    @Override
    public int order() {
        return ORDER;
    }

    @Override
    public Class<?> select() {
        return LoginContext.QrCodeCallback.Request.class;
    }

    @Override
    public Mono<Boolean> execute(ServerWebExchange exchange, Object data) {
        return Mono.just(true);
    }

}

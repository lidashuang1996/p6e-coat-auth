package club.p6e.coat.gateway.auth.validator.support;

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
@Component
//@ConditionalOnExpression(VerificationCodeObtainParameterValidator.CONDITIONAL_EXPRESSION)
public class VerificationCodeObtainParameterValidator implements ParameterValidatorInterface {

    /**
     * 执行顺序
     */
    private static final int ORDER = 0;

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.verification-code.enable:false}}";

    @Override
    public int order() {
        return ORDER;
    }

    @Override
    public Class<?> select() {
        return LoginContext.VerificationCodeObtain.Request.class;
    }

    @Override
    public Mono<Boolean> execute(ServerWebExchange exchange, Object data) {
        if (data instanceof final LoginContext.VerificationCodeObtain.Request param) {
            return Mono.just(param.getAccount() != null);
        }
        return Mono.just(false);
    }

}

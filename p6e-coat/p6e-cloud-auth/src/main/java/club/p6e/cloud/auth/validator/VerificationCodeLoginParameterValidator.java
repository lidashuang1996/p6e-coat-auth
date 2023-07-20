package club.p6e.cloud.auth.validator;

import club.p6e.cloud.auth.context.LoginContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class VerificationCodeLoginParameterValidator implements ParameterValidatorInterface {

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
        return LoginContext.VerificationCode.Request.class;
    }

    @Override
    public Mono<Boolean> execute(ServerWebExchange exchange, Object data) {
        if (data instanceof final LoginContext.VerificationCode.Request param) {
            return Mono.just(param.getCode() != null);
        }
        return Mono.just(false);
    }

}

package club.p6e.auth.validator;

import club.p6e.auth.context.ForgotPasswordContext;
import club.p6e.auth.context.LoginContext;
import club.p6e.auth.utils.VerificationUtil;
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
        return Mono.just(data instanceof final LoginContext.QrCodeCallback.Request param && param.getContent() != null);
    }

}
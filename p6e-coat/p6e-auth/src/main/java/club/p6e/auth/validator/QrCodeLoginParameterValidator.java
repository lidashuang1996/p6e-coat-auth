package club.p6e.auth.validator;

import club.p6e.auth.context.LoginContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeLoginParameterValidator implements ParameterValidatorInterface {

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
        return LoginContext.QrCode.Request.class;
    }

    @Override
    public Mono<Boolean> execute(ServerWebExchange exchange, Object data) {
        return Mono.just(data instanceof LoginContext.QrCode.Request);
    }

}
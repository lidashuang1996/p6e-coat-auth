package club.p6e.cloud.auth.controller;

import club.p6e.cloud.auth.context.RegisterContext;
import club.p6e.cloud.auth.service.RegisterService;
import club.p6e.cloud.auth.context.ResultContext;
import club.p6e.cloud.auth.validator.ParameterValidator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class RegisterControllerImpl implements RegisterController<RegisterContext.Request, ResultContext> {

    private final RegisterService service;

    public RegisterControllerImpl(RegisterService service) {
        this.service = service;
    }

    protected Mono<Void> vp(ServerWebExchange exchange, RegisterContext.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, RegisterContext.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p));
    }

}

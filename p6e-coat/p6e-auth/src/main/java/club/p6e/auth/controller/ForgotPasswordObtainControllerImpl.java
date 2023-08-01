package club.p6e.auth.controller;

import club.p6e.auth.context.ForgotPasswordContext;
import club.p6e.auth.context.RegisterContext;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.service.ForgotPasswordObtainService;
import club.p6e.auth.validator.ParameterValidator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class ForgotPasswordObtainControllerImpl
        implements ForgotPasswordObtainController<ForgotPasswordContext.Obtain.Request, ResultContext> {

    private final ForgotPasswordObtainService service;

    public ForgotPasswordObtainControllerImpl(ForgotPasswordObtainService service) {
        this.service = service;
    }

    protected Mono<Void> vp(ServerWebExchange exchange, ForgotPasswordContext.Obtain.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, ForgotPasswordContext.Obtain.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }

}

package club.p6e.auth.controller;

import club.p6e.auth.AuthPage;
import club.p6e.auth.AuthVoucher;
import club.p6e.auth.context.RegisterContext;
import club.p6e.auth.service.RegisterService;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.utils.TemplateParser;
import club.p6e.auth.validator.ParameterValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
    public Mono<Void> def(ServerWebExchange exchange) {
        final Map<String, String> m = new HashMap<>();
        m.put(AuthVoucher.REGISTER, "true");
        m.put(AuthVoucher.REGISTER_DATE, String.valueOf(System.currentTimeMillis()));
        return AuthVoucher
                .create(m)
                .flatMap(v -> write(exchange, v.getMark()));
    }


    /**
     * 写入返回数据
     *
     * @param exchange ServerWebExchange 对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> write(ServerWebExchange exchange, String voucher) {
        final AuthPage.Model register = AuthPage.register();
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(register.getType());
        return response.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(
                (TemplateParser.execute(register.getContent(), "voucher", voucher)).getBytes(StandardCharsets.UTF_8)
        )));
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, RegisterContext.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }

}

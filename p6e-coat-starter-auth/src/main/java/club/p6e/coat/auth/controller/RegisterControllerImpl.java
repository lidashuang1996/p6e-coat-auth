package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.AuthPage;
import club.p6e.coat.auth.AuthVoucher;
import club.p6e.coat.auth.context.RegisterContext;
import club.p6e.coat.auth.service.RegisterService;
import club.p6e.coat.auth.context.ResultContext;
import club.p6e.coat.auth.utils.TemplateParser;
import club.p6e.coat.auth.validator.ParameterValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 注册的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class RegisterControllerImpl implements
        RegisterController<RegisterContext.Request, ResultContext> {

    /**
     * 注册的服务对象
     */
    private final RegisterService service;

    /**
     * 构造方法初始化
     *
     * @param service 注册的服务对象
     */
    public RegisterControllerImpl(RegisterService service) {
        this.service = service;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, RegisterContext.Request param) {
        return ParameterValidator.execute(exchange, param);
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
    public Mono<Void> def(ServerWebExchange exchange) {
        return AuthVoucher
                .create(new HashMap<>() {{
                    put(AuthVoucher.REGISTER, "true");
                    put(AuthVoucher.REGISTER_DATE, String.valueOf(System.currentTimeMillis()));
                }})
                .flatMap(v -> write(exchange, v.getMark()));
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, RegisterContext.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }

}

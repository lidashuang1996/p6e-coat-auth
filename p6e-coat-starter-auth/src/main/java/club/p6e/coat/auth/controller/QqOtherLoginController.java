package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.service.QqOtherLoginService;
import club.p6e.coat.auth.AuthCertificateAuthority;
import club.p6e.coat.auth.context.ResultContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class QqOtherLoginController implements OtherLoginController {

    /**
     * QQ 第三方登录的服务对象
     */
    private final QqOtherLoginService service;

    /**
     * 认证授权的服务对象
     */
    private final AuthCertificateAuthority authority;

    /**
     * 构造方法初始化
     *
     * @param service   QQ 第三方登录的服务对象
     * @param authority 认证授权的服务对象
     */
    public QqOtherLoginController(QqOtherLoginService service, AuthCertificateAuthority authority) {
        this.service = service;
        this.authority = authority;
    }

    @GetMapping("/home")
    public Mono<Void> home(ServerWebExchange exchange) {
        return service
                .home(exchange)
                .flatMap(s -> {
                    exchange.getResponse().setStatusCode(HttpStatus.FOUND);
                    exchange.getResponse().getHeaders().setLocation(
                            UriComponentsBuilder.fromUriString(s).build().toUri()
                    );
                    return exchange.getResponse().setComplete();
                });
    }

    @GetMapping("/callback")
    public Mono<ResultContext> callback(ServerWebExchange exchange) {
        return service
                .callback(exchange)
                .flatMap(u -> authority.award(exchange, u));
    }

    @Override
    public String type() {
        return "QQ";
    }
}

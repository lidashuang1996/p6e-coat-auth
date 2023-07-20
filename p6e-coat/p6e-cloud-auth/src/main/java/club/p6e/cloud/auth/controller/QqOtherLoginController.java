package club.p6e.cloud.auth.controller;

import club.p6e.cloud.auth.service.QqOtherLoginService;
import club.p6e.cloud.auth.AuthCertificateAuthority;
import club.p6e.cloud.auth.context.ResultContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/login/other/qq")
public class QqOtherLoginController implements OtherLoginController {

    private final QqOtherLoginService service;
    /**
     * 认证授权的服务对象
     */
    private final AuthCertificateAuthority authority;

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
                .flatMap(u -> authority.present(exchange, u))
                .map(ResultContext::build);
    }

}

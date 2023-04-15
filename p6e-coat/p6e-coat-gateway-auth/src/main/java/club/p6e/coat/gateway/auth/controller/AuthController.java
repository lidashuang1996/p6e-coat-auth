package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.ResultContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final ReactiveAuthenticationManager manager;

    public AuthController(ReactiveAuthenticationManager manager) {
        this.manager = manager;
    }

    @RequestMapping("/login")
    public Mono<ResultContext> login() {
        return manager.authenticate(new UsernamePasswordAuthenticationToken("123456", "2e7605b59956f9cd7b34dbc3e6866d4a"))
                .map(authentication -> {
                    return ResultContext.build("987654321");
                });
    }

}

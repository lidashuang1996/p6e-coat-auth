package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

/**
 * @author lidashuang
 * @version 1.0.0
 */
@EnableWebFluxSecurity
@SpringBootApplication
public class P6eGatewayAuthApplication {

    public static void main(String[] args) {
        SpringUtil.init(
                SpringApplication.run(P6eGatewayAuthApplication.class, args)
        );
    }

}

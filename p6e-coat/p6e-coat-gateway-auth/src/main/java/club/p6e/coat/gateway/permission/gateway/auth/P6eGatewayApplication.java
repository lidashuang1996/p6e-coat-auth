package club.p6e.coat.gateway.permission.gateway.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

/**
 * @author lidashuang
 * @version 1.0.0
 */
@EnableWebFluxSecurity
@SpringBootApplication
public class P6eGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(P6eGatewayApplication.class, args);
    }

}

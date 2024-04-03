package club.p6e.coat.auth;

import club.p6e.coat.common.Properties;
import club.p6e.coat.common.controller.filter.CrossDomainWebFilter;
import club.p6e.coat.common.controller.filter.CrossDomainWebFluxFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * http://127.0.0.1:7322/oauth2/authorize?responseType=code&clientId=123456&redirectUri=http://127.0.0.1:9999/cb&scope=open_id,user_info&state=898989
 *
 * @author lidashuang
 * @version 1.0.0
 */
@Configuration
@SpringBootApplication
public class P6eAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(P6eAuthApplication.class, args);
    }

    @Bean
    public CrossDomainWebFluxFilter ij(Properties properties) {
        return new CrossDomainWebFluxFilter(properties);
    }

}

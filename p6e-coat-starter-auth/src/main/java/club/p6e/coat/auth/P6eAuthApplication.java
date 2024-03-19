package club.p6e.coat.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * http://127.0.0.1:7322/oauth2/authorize?responseType=code&clientId=123456&redirectUri=http://127.0.0.1:9999/cb&scope=open_id,user_info&state=898989
 *
 * @author lidashuang
 * @version 1.0.0
 */
@EnableP6eAuth
@SpringBootApplication
public class P6eAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(P6eAuthApplication.class, args);
    }

}

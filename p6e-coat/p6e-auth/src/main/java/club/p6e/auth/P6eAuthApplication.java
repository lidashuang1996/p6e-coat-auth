package club.p6e.auth;

import club.p6e.auth.service.IndexService;
import club.p6e.auth.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lidashuang
 * @version 1.0.0
 * http://127.0.0.1:8080/oauth2/auth?scope=user_info&clientId=123456&redirectUri=http://127.0.0.1:9999/cb&responseType=code&state=111
 * user_info
 */
@EnableP6eAuth
@SpringBootApplication
public class P6eAuthApplication {

    public static void main(String[] args) {
        SpringUtil.init(
                SpringApplication.run(P6eAuthApplication.class, args)
        );
        System.out.println(
                SpringUtil.getBean(AuthPasswordEncryptor.class).execute("123456")
        );
    }

}

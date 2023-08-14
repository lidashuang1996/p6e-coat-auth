package club.p6e.auth;

import club.p6e.auth.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lidashuang
 * @version 1.0.0
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

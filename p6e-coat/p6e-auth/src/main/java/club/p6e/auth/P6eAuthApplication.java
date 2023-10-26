package club.p6e.auth;

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
        SpringApplication.run(P6eAuthApplication.class, args);
        System.out.println("xxxxxxxxxxxxxx");
    }

}

package club.p6e.coat.auth.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lidashuang
 * @version 1.0
 */
@SpringBootApplication
public class P6eStarterShortLinkApplication {


    public static void main(String[] args) {
        System.out.println(
                SpringApplication.run(P6eStarterShortLinkApplication.class, args)
                // .getBean(PaymentService.class).execute(null).block()
        );
    }

}

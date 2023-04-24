package club.p6e.coat.file.manage;

import com.darvi.hksi.badminton.lib.AuthCore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lidashuang
 * @version 1.0.0
 */
@SpringBootApplication
public class P6eGatewayApplication {

    public static void main(String[] args) {
        AuthCore.setDebug(true);
        SpringApplication.run(P6eGatewayApplication.class, args);
    }

}

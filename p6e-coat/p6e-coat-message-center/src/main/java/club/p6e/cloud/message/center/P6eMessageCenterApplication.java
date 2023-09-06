package club.p6e.cloud.message.center;

import com.darvi.hksi.badminton.lib.AuthCore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class P6eMessageCenterApplication {

    public static void main(String[] args) {
        AuthCore.setDebug(true);
        SpringApplication.run(P6eMessageCenterApplication.class, args);
    }

}

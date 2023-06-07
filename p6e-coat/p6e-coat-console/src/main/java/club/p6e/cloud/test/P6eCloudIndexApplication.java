package club.p6e.cloud.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class P6eCloudIndexApplication {

    public static void main(String[] args) {
        SpringApplication.run(P6eCloudIndexApplication.class, args);

    }

}

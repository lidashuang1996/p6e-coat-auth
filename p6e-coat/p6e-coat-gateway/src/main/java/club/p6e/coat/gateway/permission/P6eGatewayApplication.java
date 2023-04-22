package club.p6e.coat.gateway.permission;

import club.p6e.coat.gateway.permission.file.EnableP6eFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableP6eFile
@SpringBootApplication
public class P6eGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(P6eGatewayApplication.class, args);
    }

}

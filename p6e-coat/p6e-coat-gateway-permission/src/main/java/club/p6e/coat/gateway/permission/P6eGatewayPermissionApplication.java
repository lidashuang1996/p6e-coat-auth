package club.p6e.coat.gateway.permission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableP6eGatewayPermission
@SpringBootApplication
public class P6eGatewayPermissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(P6eGatewayPermissionApplication.class, args);
    }

}

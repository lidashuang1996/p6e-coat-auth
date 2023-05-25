package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.controller.AuthController;
import club.p6e.coat.gateway.auth.service.*;
import club.p6e.coat.gateway.auth.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

/**
 * @author lidashuang
 * @version 1.0.0
 */
@EnableWebFluxSecurity
@SpringBootApplication
public class P6eGatewayAuthApplication {

    public static void main(String[] args) {
        SpringUtil.init(
                SpringApplication.run(P6eGatewayAuthApplication.class, args)
        );

        SpringUtil.getBean(AuthController.class)
                .setAccountPasswordLoginService(SpringUtil.getBean(AccountPasswordLoginService.class));
        SpringUtil.getBean(AuthController.class)
                .setAccountPasswordLoginSignatureService(SpringUtil.getBean(AccountPasswordLoginSignatureService.class));


        SpringUtil.getBean(AuthController.class)
                .setVerificationCodeLoginService(SpringUtil.getBean(VerificationCodeLoginService.class));
        SpringUtil.getBean(AuthController.class)
                .setVerificationCodeObtainService(SpringUtil.getBean(VerificationCodeObtainService.class));


        SpringUtil.getBean(AuthController.class)
                .setQuickResponseCodeLoginService(SpringUtil.getBean(QuickResponseCodeLoginService.class));
        SpringUtil.getBean(AuthController.class)
                .setQuickResponseCodeObtainService(SpringUtil.getBean(QuickResponseCodeObtainService.class));
    }

}

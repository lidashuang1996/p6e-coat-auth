package club.p6e.coat.gateway;

import club.p6e.coat.gateway.auth.AuthJsonWebTokenCipher;
import club.p6e.coat.gateway.auth.EnableP6eGatewayAuth;
import club.p6e.coat.gateway.auth.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@EnableP6eGatewayAuth
@SpringBootApplication
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "club.p6e.coat.gateway.auth..*")
})
public class P6eGatewayApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(P6eGatewayApplication.class, args);
        SpringUtil.init(context);

        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            if (beanDefinitionName.startsWith("org.springframework") || beanDefinitionName.startsWith("spring.")) {

            } else {
                System.out.println(
                        beanDefinitionName
                );
            }
        }

        System.out.println(
                SpringUtil.getBean(AuthJsonWebTokenCipher.class)
        );

    }

}

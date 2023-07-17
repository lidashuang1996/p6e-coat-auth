package club.p6e.coat.gateway.auth;

import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({
        Properties.class,
        Configuration.class,
        AutoConfigureImportSelector2.class
})
public @interface EnableP6eGatewayAuth {
}

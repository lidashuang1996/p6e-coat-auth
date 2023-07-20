package club.p6e.cloud.auth;

import org.springframework.context.annotation.Import;

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
        AutoConfigureImportSelector.class
})
public @interface EnableP6eGatewayAuth {
}

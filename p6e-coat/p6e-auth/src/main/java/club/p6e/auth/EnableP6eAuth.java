package club.p6e.auth;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

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
public @interface EnableP6eAuth {
}

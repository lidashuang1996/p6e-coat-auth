package club.p6e.coat.gateway.auth.repository.support;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;

/**
 * R2DBC 配置
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@Configuration
public class R2dbcConfig {

    @Bean
    @ConditionalOnMissingBean(
            value = R2dbcEntityTemplate.class,
            ignored = R2dbcEntityTemplate.class
    )
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }

}

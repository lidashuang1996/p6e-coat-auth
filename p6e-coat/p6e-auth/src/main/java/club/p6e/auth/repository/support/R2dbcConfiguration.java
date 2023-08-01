package club.p6e.auth.repository.support;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.reactive.TransactionalOperator;

/**
 * @author lidashuang
 * @version 1.0
 */
@Configuration
public class R2dbcConfiguration {

    @Bean
    public R2dbcEntityTemplate r1(ConnectionFactory connectionFactory) {
        System.out.println("R1R1R1R1R1R1RR1R1");
        return new R2dbcEntityTemplate(connectionFactory);
    }

    @Bean
    public TransactionalOperator r2(ConnectionFactory connectionFactory) {
        System.out.println("R2R22R2R2R2R2R2R2R2R2R");
        return TransactionalOperator.create(new R2dbcTransactionManager(connectionFactory));
    }

}

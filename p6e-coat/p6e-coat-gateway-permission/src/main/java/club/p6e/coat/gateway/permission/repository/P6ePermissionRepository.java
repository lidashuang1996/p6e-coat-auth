package club.p6e.coat.gateway.permission.repository;

import club.p6e.coat.gateway.permission.model.P6ePermissionModel;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Result;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class P6ePermissionRepository {

    private static final String SQL = "" +
            "" +
            "" +
            "";
    private final R2dbcEntityTemplate template;

    public P6ePermissionRepository(R2dbcEntityTemplate template) {
        this.template = template;
    }

    public Flux<P6ePermissionModel> findAll() {
        return template
                .getDatabaseClient()
                .sql("")
                .fetch()
                .all()
                .map(s -> {
                    return new P6ePermissionModel();
                });
    }


}

package club.p6e.coat.gateway.permission.repository;

import club.p6e.coat.gateway.permission.model.PermissionModel;
import club.p6e.coat.gateway.permission.model.PermissionUrlModel;
import io.r2dbc.postgresql.message.frontend.Query;
import io.r2dbc.spi.Batch;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class PermissionRepository {

    @SuppressWarnings("ALL")
    private static final String SQL = "" +
            "SELECT   " +
            "   *   " +
            "FROM   " +
            "   (   " +
            "       SELECT   " +
            "           *   " +
            "       FROM   " +
            "           p6e_permission_url   " +
            "       LIMIT $1 OFFSET $2   " +
            "   ) AS \"TU\"   " +
            "   LEFT JOIN   " +
            "       p6e_permission_url_group_relation_url AS \"TR\"   " +
            "   ON   " +
            "       \"TU\".\"id\" = \"TR\".\"uid\"   " +
            "   LEFT JOIN   " +
            "       p6e_permission_url_group AS \"TG\"   " +
            "   ON   " +
            "       \"TR\".\"gid\" = \"TG\".\"id\"   " +
            ";";
    private final R2dbcEntityTemplate template;

    public PermissionRepository(R2dbcEntityTemplate template) {
        this.template = template;
    }

    /**
     * @param page
     * @param size
     * @return
     */
    public Flux<PermissionModel> findByPermissionUrlTableAll(Integer page, Integer size) {
        page = page == null ? 1 : (page <= 0 ? 1 : page);
        size = size == null ? 1 : (size <= 0 ? 1 : (size > 200 ? 200 : size));
        return template
                .getDatabaseClient()
                .sql(SQL)
                .bind(1, (page - 1) * size)
                .bind(2, size)
                .fetch()
                .all()
                .map(m -> {
                    return new PermissionModel();
                });
    }


}

package club.p6e.coat.gateway.permission.repository;

import club.p6e.coat.gateway.permission.model.PermissionModel;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class PermissionRepository {

    /**
     * SQL
     */
    @SuppressWarnings("ALL")
    private static final String SQL = "" +
            "SELECT   " +
            "   \"TU\".\"id\",   " +
            "   \"TU\".\"url\",   " +
            "   \"TU\".\"base_url\",   " +
            "   \"TU\".\"method\",   " +
            "   \"TU\".\"name\",   " +
            "   \"TU\".\"config\",   " +
            "   \"TG\".\"id\",   " +
            "   \"TG\".\"weight\",   " +
            "   \"TG\".\"mark\",   " +
            "   \"TG\".\"name\",   " +
            "   \"TR\".\"config\",   " +
            "   \"TR\".\"attribute\"    " +
            "FROM   " +
            "   (   " +
            "       SELECT   " +
            "           \"id\",   " +
            "           \"url\",   " +
            "           \"base_url\",   " +
            "           \"method\",   " +
            "           \"name\",   " +
            "           \"config\"   " +
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

    /**
     * 模板对象
     */
    private final R2dbcEntityTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 模板对象
     */
    public PermissionRepository(R2dbcEntityTemplate template) {
        this.template = template;
    }

    /**
     * 查询权限 URL 表格对应的全部数据
     *
     * @param page 分页页码
     * @param size 分页长度
     * @return Flux/PermissionModel 查询数据结果
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
                    System.out.println(
                            m
                    );
                    final PermissionModel permission = new PermissionModel();
                    permission.setUid(null);
                    permission.setUUrl(null);
                    permission.setUBaseUrl(null);
                    permission.setUMethod(null);
                    permission.setUName(null);
                    permission.setUConfig(null);
                    permission.setGid(null);
                    permission.setGWeight(null);
                    permission.setGMark(null);
                    permission.setGName(null);
                    permission.setRConfig(null);
                    permission.setRAttribute(null);
                    return permission;
                });
    }


}

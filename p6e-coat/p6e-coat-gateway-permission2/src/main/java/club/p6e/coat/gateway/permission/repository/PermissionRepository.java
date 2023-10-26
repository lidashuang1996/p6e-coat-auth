package club.p6e.coat.gateway.permission.repository;

import club.p6e.coat.gateway.permission.PermissionDetails;
import club.p6e.coat.gateway.permission.utils.TransformationUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = PermissionRepository.class,
        ignored = PermissionRepository.class
)
public class PermissionRepository {

    /**
     * SQL
     */
    @SuppressWarnings("ALL")
    private static final String SQL = "" +
            "SELECT   " +
            "   \"TU\".\"id\" AS u_id,  " +
            "   \"TU\".\"url\" AS u_url,   " +
            "   \"TU\".\"base_url\" AS u_base_url,   " +
            "   \"TU\".\"method\" AS u_method,   " +
            "   \"TU\".\"name\" AS u_name,   " +
            "   \"TU\".\"config\" AS u_config,   " +
            "   \"TG\".\"id\" AS g_id,   " +
            "   \"TG\".\"weight\" AS g_weight,   " +
            "   \"TG\".\"mark\" AS g_mark,   " +
            "   \"TG\".\"name\" AS g_name,   " +
            "   \"TR\".\"config\" AS r_config,   " +
            "   \"TR\".\"attribute\" AS r_attribute    " +
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
            "           \"p6e_permission_url\"    " +
            "       ORDER BY \"id\" ASC" +
            "       LIMIT :limit OFFSET :offset   " +
            "   ) AS \"TU\"   " +
            "   LEFT JOIN   " +
            "       \"p6e_permission_url_group_relation_url\" AS \"TR\"   " +
            "   ON   " +
            "       \"TU\".\"id\" = \"TR\".\"uid\"   " +
            "   LEFT JOIN   " +
            "       \"p6e_permission_url_group\" AS \"TG\"   " +
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
    public Flux<PermissionDetails> findByPermissionUrlTableAll(Integer page, Integer size) {
        page = page == null ? 1 : (page <= 0 ? 1 : page);
        size = size == null ? 16 : (size <= 0 ? 16 : (size > 200 ? 200 : size));
        final String sql = SQL
                .replace(":limit", String.valueOf(size))
                .replace(":offset", String.valueOf((page - 1) * size));
        return template
                .getDatabaseClient()
                .sql(sql)
                .fetch()
                .all()
                .map(m -> {
                    final PermissionDetails permission = new PermissionDetails();
                    permission.setUid(TransformationUtil.objectToInteger(m.get("u_id")));
                    permission.setUUrl(TransformationUtil.objectToString(m.get("u_url")));
                    permission.setUBaseUrl(TransformationUtil.objectToString(m.get("u_base_url")));
                    permission.setUMethod(TransformationUtil.objectToString(m.get("u_method")));
                    permission.setUName(TransformationUtil.objectToString(m.get("u_name")));
                    permission.setUConfig(TransformationUtil.objectToString(m.get("u_config")));
                    permission.setGid(TransformationUtil.objectToInteger(m.get("g_id")));
                    permission.setGWeight(TransformationUtil.objectToInteger(m.get("g_weight")));
                    permission.setGMark(TransformationUtil.objectToString(m.get("g_mark")));
                    permission.setGName(TransformationUtil.objectToString(m.get("g_name")));
                    permission.setRConfig(TransformationUtil.objectToString(m.get("r_config")));
                    permission.setRAttribute(TransformationUtil.objectToString(m.get("r_attribute")));
                    return permission;
                });
    }


}

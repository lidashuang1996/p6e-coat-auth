package club.p6e.coat.gateway.auth.repository;

import club.p6e.coat.gateway.auth.model.UserAuthModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 用户认证模型
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = UserAuthRepository.class,
        ignored = UserAuthRepository.class
)
public class UserAuthRepository {

    /**
     * 模板对象
     */
    private final R2dbcEntityTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 模板对象
     */
    public UserAuthRepository(R2dbcEntityTemplate template) {
        this.template = template;
    }

    /**
     * 根据 ID 查询一条数据
     *
     * @param id ID
     * @return Mono/UserAuthModel 用户认证模型对象
     */
    public Mono<UserAuthModel> findOneById(Integer id) {
        return template.selectOne(
                Query.query(Criteria.where(UserAuthModel.ID).is(id).and(UserAuthModel.IS_DELETE).is(0)),
                UserAuthModel.class
        );
    }

}

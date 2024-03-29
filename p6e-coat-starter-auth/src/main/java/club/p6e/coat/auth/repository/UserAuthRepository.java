package club.p6e.coat.auth.repository;

import club.p6e.coat.auth.model.UserAuthModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * @author lidashuang
 * @version 1.0
 */
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
    public Mono<UserAuthModel> findById(Integer id) {
        return template.selectOne(
                Query.query(Criteria.where(UserAuthModel.ID).is(id)),
                UserAuthModel.class
        );
    }

    public Mono<UserAuthModel> findByAccount(String account) {
        return template.selectOne(
                Query.query(Criteria
                        .where(UserAuthModel.ACCOUNT).is(account)

                ), UserAuthModel.class
        );
    }

    public Mono<UserAuthModel> findByPhone(String account) {
        return template.selectOne(
                Query.query(Criteria
                        .where(UserAuthModel.PHONE).is(account)

                ), UserAuthModel.class
        );
    }

    public Mono<UserAuthModel> findByMailbox(String account) {
        return template.selectOne(
                Query.query(Criteria
                        .where(UserAuthModel.MAILBOX).is(account)

                ), UserAuthModel.class
        );
    }

    public Mono<UserAuthModel> findByPhoneOrMailbox(String account) {
        return template.selectOne(
                Query.query(Criteria
                        .where(UserAuthModel.PHONE).is(account)
                        .or(UserAuthModel.MAILBOX).is(account)

                ), UserAuthModel.class
        );
    }

    /**
     * 根据 ID 查询一条数据
     *
     * @param id ID
     * @return Mono/UserAuthModel 用户认证模型对象
     */
    public Mono<UserAuthModel> findByQq(String qq) {
        return template.selectOne(
                Query.query(Criteria.where(UserAuthModel.QQ).is(qq)),
                UserAuthModel.class
        );
    }


    public Mono<UserAuthModel> create(UserAuthModel model) {
        model
                .setVersion(0)
                .setOperator("register_sys")
                .setCreateDate(LocalDateTime.now())
                .setUpdateDate(LocalDateTime.now());
        return template.insert(model);
    }

    public Mono<Long> updatePassword(Integer id, String password) {
        return template.update(
                Query.query(Criteria.where(UserAuthModel.ID).is(id)),
                Update.update(UserAuthModel.PASSWORD, password),
                UserAuthModel.class
        );
    }
}

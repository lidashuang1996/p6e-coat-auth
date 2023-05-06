package club.p6e.coat.gateway.auth.repository;

import club.p6e.coat.gateway.auth.model.UserModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = UserRepository.class,
        ignored = UserRepository.class
)
public class UserRepository {

    /**
     * 模板对象
     */
    private final R2dbcEntityTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 模板对象
     */
    public UserRepository(R2dbcEntityTemplate template) {
        this.template = template;
    }

    public Mono<UserModel> findOneById(Integer id) {
        return template.selectOne(
                Query.query(Criteria.where(UserModel.ID).is(id).and(UserModel.IS_DELETE).is(0)),
                UserModel.class
        );
    }

    public Mono<UserModel> findOneByAccount(String account) {
        return template.selectOne(
                Query.query(Criteria.where(UserModel.ACCOUNT).is(account).and(UserModel.IS_DELETE).is(0)),
                UserModel.class
        );
    }

    public Mono<UserModel> findOneByPhone(String phone) {
        return template.selectOne(
                Query.query(Criteria.where(UserModel.PHONE).is(phone).and(UserModel.IS_DELETE).is(0)),
                UserModel.class
        );
    }

    public Mono<UserModel> findOneByMailbox(String mailbox) {
        return template.selectOne(
                Query.query(Criteria.where(UserModel.MAILBOX).is(mailbox).and(UserModel.IS_DELETE).is(0)),
                UserModel.class
        );
    }

    public Mono<UserModel> findOneByPhoneOrMailbox(String phone) {
        return template.selectOne(
                Query.query(Criteria.where(UserModel.PHONE).is(phone).and(UserModel.IS_DELETE).is(0)),
                UserModel.class
        );
    }


}

package club.p6e.auth.repository;

import club.p6e.auth.model.UserModel;
import club.p6e.auth.utils.GeneratorUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户模型存储库
 *
 * @author lidashuang
 * @version 1.0
 */
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

    /**
     * 根据 ID 查询数据
     *
     * @param id ID
     * @return Mono/UserModel 用户模型对象
     */
    public Mono<UserModel> findById(Integer id) {
        return template.selectOne(
                Query.query(Criteria.where(UserModel.ID).is(id).and(UserModel.IS_DELETE).is(0)),
                UserModel.class
        );
    }

    /**
     * 根据账号查询数据
     *
     * @param account 账号
     * @return Mono/UserModel 用户模型对象
     */
    public Mono<UserModel> findByAccount(String account) {
        return template.selectOne(
                Query.query(Criteria.where(UserModel.ACCOUNT).is(account).and(UserModel.IS_DELETE).is(0)),
                UserModel.class
        );
    }

    public Mono<UserModel> createAccount(UserModel model) {
        model
                .setId(null)
                .setStatus(0)
                .setEnabled(1)
                .setPhone(null)
                .setMailbox(null)
                .setName(GeneratorUtil.uuid())
                .setNickname(GeneratorUtil.uuid())
                .setAvatar("default.jpg")
                .setDescribe("")
                .setVersion(0)
                .setIsDelete(0)
                .setOperator("register_sys")
                .setCreateDate(LocalDateTime.now())
                .setUpdateDate(LocalDateTime.now());
        return template.insert(model);
    }

    /**
     * 根据手机号码查询数据
     *
     * @param phone 手机号码
     * @return Mono/UserModel 用户模型对象
     */
    public Mono<UserModel> findByPhone(String phone) {
        return template.selectOne(
                Query.query(Criteria.where(UserModel.PHONE).is(phone).and(UserModel.IS_DELETE).is(0)),
                UserModel.class
        );
    }

    public Mono<UserModel> createPhone(UserModel model) {
        model
                .setId(null)
                .setStatus(0)
                .setEnabled(1)
                .setAccount(null)
                .setMailbox(null)
                .setName(GeneratorUtil.uuid())
                .setNickname(GeneratorUtil.uuid())
                .setAvatar("default.jpg")
                .setDescribe("")
                .setVersion(0)
                .setIsDelete(0)
                .setOperator("register_sys")
                .setCreateDate(LocalDateTime.now())
                .setUpdateDate(LocalDateTime.now());
        return template.insert(model);
    }

    /**
     * 根据邮箱查询数据
     *
     * @param mailbox 邮箱
     * @return Mono/UserModel 用户模型对象
     */
    public Mono<UserModel> findByMailbox(String mailbox) {
        return template.selectOne(
                Query.query(Criteria.where(UserModel.MAILBOX).is(mailbox).and(UserModel.IS_DELETE).is(0)),
                UserModel.class
        );
    }

    public Mono<UserModel> createMailbox(UserModel model) {
        model
                .setId(null)
                .setStatus(0)
                .setEnabled(1)
                .setPhone(null)
                .setAccount(null)
                .setName(GeneratorUtil.uuid())
                .setNickname(GeneratorUtil.uuid())
                .setAvatar("default.jpg")
                .setDescribe("")
                .setVersion(0)
                .setIsDelete(0)
                .setOperator("register_sys")
                .setCreateDate(LocalDateTime.now())
                .setUpdateDate(LocalDateTime.now());
        return template.insert(model);
    }



    /**
     * 根据手机号码或者邮箱查询一条数据
     *
     * @param content ID
     * @return Mono/UserModel 用户模型对象
     */
    public Mono<UserModel> findByPhoneOrMailbox(String content) {
        return template.selectOne(
                Query.query(Criteria.where(UserModel.IS_DELETE).is(0)
                        .and(Criteria.empty().or(
                                List.of(
                                        Criteria.where(UserModel.PHONE).is(content),
                                        Criteria.where(UserModel.MAILBOX).is(content)
                                )
                        ))),
                UserModel.class
        );
    }

    public Mono<UserModel> createPhoneOrMailbox(UserModel model) {
        model
                .setId(null)
                .setStatus(0)
                .setEnabled(1)
                .setAccount(null)
                .setName(GeneratorUtil.uuid())
                .setNickname(GeneratorUtil.uuid())
                .setAvatar("default.jpg")
                .setDescribe("")
                .setVersion(0)
                .setIsDelete(0)
                .setOperator("register_sys")
                .setCreateDate(LocalDateTime.now())
                .setUpdateDate(LocalDateTime.now());
        return template.insert(model);
    }

}

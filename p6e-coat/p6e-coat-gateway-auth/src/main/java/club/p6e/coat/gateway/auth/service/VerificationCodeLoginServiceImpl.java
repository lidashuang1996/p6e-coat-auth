package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.VerificationCodeLoginCache;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * 验证码登录服务的实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = VerificationCodeLoginService.class,
//        ignored = VerificationCodeLoginServiceImpl.class
//)
//@ConditionalOnExpression(VerificationCodeLoginService.CONDITIONAL_EXPRESSION)
public class VerificationCodeLoginServiceImpl implements VerificationCodeLoginService {

    /**
     * 验证码缓存对象
     */
    private final VerificationCodeLoginCache cache;

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 用户存储库
     */
    private final UserRepository repository;

    /**
     * 构造方法初始化
     *
     * @param cache      验证码缓存对象
     * @param properties 配置文件对象
     * @param repository 用户存储库
     */
    public VerificationCodeLoginServiceImpl(
            VerificationCodeLoginCache cache,
            Properties properties,
            UserRepository repository) {
        this.cache = cache;
        this.properties = properties;
        this.repository = repository;
    }

//    @Override
//    public LoginContext.VerificationCode.Dto execute(LoginContext.VerificationCode.Request param) {
//        // 读取配置文件判断服务是否启动
//        if (!properties.getLogin().isEnable()
//                || !properties.getLogin().getVerificationCode().isEnable()) {
//            throw GlobalExceptionContext.executeServiceNotEnableException(
//                    this.getClass(), "fun execute(LoginContext.VerificationCode.Request param).");
//        }
//        final String userId = param.getVoucherMap() == null ?
//                null : param.getVoucherMap().get(VoucherConversation.USER_ID);
//        final String accountType = param.getVoucherMap() == null ?
//                null : param.getVoucherMap().get(VoucherConversation.ACCOUNT_TYPE);
//        if (userId == null || accountType == null) {
//            throw GlobalExceptionContext.executeVoucherException(
//                    this.getClass(), "fun execute(LoginContext.VerificationCode.Request param).");
//        } else {
//            final String code = param.getCode();
//            final Optional<List<String>> codeListOptional = cache.get(accountType, userId);
//            if (codeListOptional.isEmpty()) {
//                throw GlobalExceptionContext.executeCodeLoginException(
//                        this.getClass(), "fun execute(LoginContext.VerificationCode.Request param).");
//            } else {
//                if (codeListOptional.get().contains(code)) {
//                    try {
//                        final Optional<UserModel> userOptional = repository.findById(Integer.valueOf(userId));
//                        if (userOptional.isEmpty()) {
//                            throw GlobalExceptionContext.executeUserNotExistException(
//                                    this.getClass(), "fun execute(LoginContext.VerificationCode.Request param).");
//                        } else {
//                            return CopyUtil.run(userOptional.get(), LoginContext.VerificationCode.Dto.class);
//                        }
//                    } finally {
//                        cache.del(accountType, userId, code);
//                    }
//                } else {
//                    throw GlobalExceptionContext.executeCodeLoginException(
//                            this.getClass(), "fun execute(LoginContext.VerificationCode.Request param).");
//                }
//            }
//        }
//    }

    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getVerificationCode().isEnable();
    }

    @Override
    public Mono<AuthUserDetails> execute(LoginContext.VerificationCode.Request param) {
//        return Mono
//                .just(isEnable())
//                .filter(b -> b)
//                .flatMap(c -> cache.get())
        return null;
    }
}

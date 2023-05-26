package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.QrCodeLoginCache;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * 二维码登录服务的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = QrCodeLoginService.class,
//        ignored = QrCodeLoginServiceImpl.class
//)
//@ConditionalOnExpression(QrCodeLoginService.CONDITIONAL_EXPRESSION)
public class QrCodeLoginServiceImpl implements QrCodeLoginService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 二维码缓存对象
     */
    private final QrCodeLoginCache cache;

    /**
     * 用户存储库
     */
    private final UserRepository repository;

    /**
     * 构造方法初始化
     *
     * @param cache      二维码缓存对象
     * @param properties 配置文件对象
     * @param repository 用户存储库
     */
    public QrCodeLoginServiceImpl(
            Properties properties,
            QrCodeLoginCache cache,
            UserRepository repository) {
        this.cache = cache;
        this.properties = properties;
        this.repository = repository;
    }

    @Override
    public Mono<AuthUserDetails> execute(ServerWebExchange exchange, LoginContext.QrCode.Request param) {
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> {
                    final String mark = v.get(AuthVoucher.QUICK_RESPONSE_CODE_LOGIN_MARK);
                    return cache
                            .get(mark)
                            .switchIfEmpty(Mono.error(GlobalExceptionContext.executeCacheException(
                                    this.getClass(),
                                    "fun execute(ServerWebExchange exchange, LoginContext.QrCode.Request param)",
                                    "QrCode login cache data does not exist or expire exception."
                            )))
                            .flatMap(s -> {
                                if (QrCodeLoginCache.isEmpty(s)) {
                                    return Mono.error(GlobalExceptionContext.executeQrCodeDataNullException(
                                            this.getClass(),
                                            "fun execute(ServerWebExchange exchange, LoginContext.QrCode.Request param)",
                                            "QrCode login data is null exception."
                                    ));
                                } else {
                                    return cache
                                            .del(mark)
                                            .flatMap(l -> repository
                                                    .findOneById(Integer.valueOf(s))
                                                    .flatMap(m -> {
                                                        if (m == null) {
                                                            return Mono.error(GlobalExceptionContext.executeUserNotExistException(
                                                                    this.getClass(),
                                                                    "fun execute(ServerWebExchange exchange, LoginContext.QrCode.Request param)",
                                                                    "QrCode login user id select data does not exist exception."
                                                            ));
                                                        } else {
                                                            return Mono.just(new AuthUserDetails(m));
                                                        }
                                                    }));
                                }
                            });
                });
    }
}

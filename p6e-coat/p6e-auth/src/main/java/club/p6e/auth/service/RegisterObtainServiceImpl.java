package club.p6e.auth.service;

import club.p6e.auth.AuthVoucher;
import club.p6e.auth.cache.RegisterCodeCache;
import club.p6e.auth.context.RegisterContext;
import club.p6e.auth.generator.RegisterCodeGenerator;
import club.p6e.auth.launcher.Launcher;
import club.p6e.auth.launcher.LauncherType;
import club.p6e.auth.utils.VerificationUtil;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class RegisterObtainServiceImpl implements RegisterObtainService {

    /**
     * CODE LOGIN 模板
     */
    private static final String REGISTER_TEMPLATE = "REGISTER_TEMPLATE";

    private final RegisterCodeCache cache;
    private final RegisterCodeGenerator generator;

    public RegisterObtainServiceImpl(RegisterCodeCache cache, RegisterCodeGenerator generator) {
        this.cache = cache;
        this.generator = generator;
    }

    @Override
    public Mono<RegisterContext.Obtain.Dto> execute(ServerWebExchange exchange, RegisterContext.Obtain.Request param) {
        final String account = param.getAccount();
        final boolean pb = VerificationUtil.phone(param.getAccount());
        final boolean mb = VerificationUtil.mailbox(param.getAccount());
        final LauncherType type = pb ? LauncherType.SMS : mb ? LauncherType.EMAIL : null;
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> {
                    final String code = generator.execute();
                    return cache
                            .set(account, code)
                            .flatMap(b -> {
                                final Map<String, String> map = new HashMap<>();
                                map.put("code", code);
                                return Launcher.push(type, account, REGISTER_TEMPLATE, map);
                            });
                })
                .map(m -> new RegisterContext.Obtain.Dto().setAccount(account).setMessage(m));
    }

}

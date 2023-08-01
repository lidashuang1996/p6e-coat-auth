package club.p6e.auth.service;

import club.p6e.auth.AuthVoucher;
import club.p6e.auth.cache.ForgotPasswordCodeCache;
import club.p6e.auth.context.ForgotPasswordContext;
import club.p6e.auth.generator.ForgotPasswordCodeGenerator;
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
public class ForgotPasswordObtainServiceImpl implements ForgotPasswordObtainService {

    /**
     * CODE LOGIN 模板
     */
    private static final String FORGOT_PASSWORD_TEMPLATE = "FORGOT_PASSWORD_TEMPLATE";

    private final ForgotPasswordCodeCache cache;
    private final ForgotPasswordCodeGenerator generator;

    public ForgotPasswordObtainServiceImpl(ForgotPasswordCodeCache cache, ForgotPasswordCodeGenerator generator) {
        this.cache = cache;
        this.generator = generator;
    }

    @Override
    public Mono<ForgotPasswordContext.Obtain.Dto> execute(
            ServerWebExchange exchange, ForgotPasswordContext.Obtain.Request param) {
        final String account = param.getAccount();
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> {
                    final String code = generator.execute();
                    final boolean pb = VerificationUtil.phone(param.getAccount());
                    final boolean mb = VerificationUtil.mailbox(param.getAccount());
                    final LauncherType type = pb ? LauncherType.SMS : mb ? LauncherType.EMAIL : null;
                    return cache
                            .set(account, code)
                            .flatMap(b -> {
                                final Map<String, String> map = new HashMap<>();
                                map.put("code", code);
                                return Launcher.push(type, account, FORGOT_PASSWORD_TEMPLATE, map);
                            });
                })
                .map(m -> new ForgotPasswordContext.Obtain.Dto().setAccount(account).setMessage(m));
    }


}

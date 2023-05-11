package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthForeignMinistry;
import club.p6e.coat.gateway.auth.AuthForeignMinistryVisaTemplate;
import club.p6e.coat.gateway.auth.JsonSerializeDeserializeAuthentication;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.authentication.AccountPasswordAuthenticationVoucherToken;
import club.p6e.coat.gateway.auth.authentication.QuickResponseCodeAuthenticationVoucherToken;
import club.p6e.coat.gateway.auth.authentication.VerificationCodeAuthenticationVoucherToken;
import club.p6e.coat.gateway.auth.context.AccountPasswordLoginContext;
import club.p6e.coat.gateway.auth.context.QRCodeLoginContext;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.context.VerificationCodeLoginContext;
import club.p6e.coat.gateway.auth.error.ParameterException;
import club.p6e.coat.gateway.auth.error.ServiceNotEnabledException;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 认证
 *
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 认证外交部
     */
    private final AuthForeignMinistry authForeignMinistry;

    /**
     * 认证管理器
     */
    private final ReactiveAuthenticationManager authenticationManager;

    /**
     * 构造方法
     *
     * @param properties 配置文件对象
     * @param authForeignMinistry
     * @param authenticationManager
     */
    public AuthController(
            Properties properties,
            AuthForeignMinistry authForeignMinistry
    ) {
        this.properties = properties;
        this.authForeignMinistry = authForeignMinistry;
        this.authenticationManager = null;
    }

    /**
     * 账号密码登录
     */
    @PostMapping("/login")
    public Mono<ResultContext> login(
            ServerHttpRequest request,
            ServerHttpResponse response,
            @RequestBody AccountPasswordLoginContext.Request param
    ) {
        if (properties.getLogin().getAccountPassword().isEnable()) {
            if (param == null
                    || param.getVoucher() == null
                    || param.getAccount() == null
                    || param.getPassword() == null) {
                return Mono.error(new ParameterException(this.getClass(), "", ""));
            }
            final AccountPasswordAuthenticationVoucherToken vt =
                    AccountPasswordAuthenticationVoucherToken.create(
                            param.getVoucher(), param.getAccount(), param.getPassword());
            return authenticationManager
                    .authenticate(vt)
                    .flatMap(authentication -> authForeignMinistry.apply(
                            request, response, AuthForeignMinistryVisaTemplate.create((JsonSerializeDeserializeAuthentication) authentication)))
                    .map(ResultContext::build);
        } else {
            return Mono.error(new ServiceNotEnabledException(this.getClass(), "", ""));
        }
    }

    /**
     * 验证码登录
     */
    @PostMapping("/login/verification_code")
    public Mono<ResultContext> verificationCodeLogin(
            ServerHttpRequest request,
            ServerHttpResponse response,
            @RequestBody VerificationCodeLoginContext.Request param
    ) {
        if (properties.getLogin().getVerificationCode().isEnable()) {
            if (param == null
                    || param.getKey() == null
                    || param.getValue() == null
                    || param.getVoucher() == null) {
                return Mono.error(new ParameterException(this.getClass(), "", ""));
            }
            final VerificationCodeAuthenticationVoucherToken vt =
                    VerificationCodeAuthenticationVoucherToken.create(
                            param.getVoucher(), param.getKey(), param.getValue());
            return authenticationManager
                    .authenticate(vt)
                    .flatMap(authentication -> authForeignMinistry.apply(
                            request, response, AuthForeignMinistryVisaTemplate.create((JsonSerializeDeserializeAuthentication) authentication)))
                    .map(ResultContext::build);
        } else {
            return Mono.error(new ServiceNotEnabledException(this.getClass(), "", ""));
        }
    }

    /**
     * 二维码登录
     */
    @PostMapping("/login/quick_response_code")
    public Mono<ResultContext> quickResponseCodeLogin(
            ServerHttpRequest request,
            ServerHttpResponse response,
            @RequestBody QRCodeLoginContext.Request param
    ) {
        if (properties.getLogin().getQrCode().isEnable()) {
            if (param == null
                    || param.getContent() == null
                    || param.getVoucher() == null) {
                return Mono.error(new ParameterException(this.getClass(), "", ""));
            }
            final QuickResponseCodeAuthenticationVoucherToken vt =
                    QuickResponseCodeAuthenticationVoucherToken.create(param.getVoucher(), param.getContent());
            return authenticationManager
                    .authenticate(vt)
                    .flatMap(authentication -> authForeignMinistry.apply(
                            request, response, AuthForeignMinistryVisaTemplate.create((JsonSerializeDeserializeAuthentication) authentication)))
                    .map(ResultContext::build);
        } else {
            return Mono.error(new ServiceNotEnabledException(this.getClass(), "", ""));
        }
    }

}

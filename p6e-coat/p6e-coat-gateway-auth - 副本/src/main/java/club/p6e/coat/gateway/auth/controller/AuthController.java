package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.certificate.AuthCertificate;
import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.AccountPasswordLoginContext;
import club.p6e.coat.gateway.auth.context.QuickResponseCodeContext;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.context.VerificationCodeLoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.error.ParameterException;
import club.p6e.coat.gateway.auth.service.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
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

    private final AuthCertificate certificate;

    private final Oauth2CodeService oauth2CodeService;

    /**
     * 账号密码登录服务对象
     */
    private AccountPasswordLoginService accountPasswordLoginService;

    /**
     * 账号密码登录签名服务对象
     */
    private AccountPasswordLoginSignatureService accountPasswordLoginSignatureService;

    /**
     * 验证码登录服务对象
     */
    private VerificationCodeLoginService verificationCodeLoginService;

    /**
     * 验证码获取服务对象
     */
    private VerificationCodeObtainService verificationCodeObtainService;

    /**
     * 二维码登录服务对象
     */
    private QuickResponseCodeLoginService quickResponseCodeLoginService;

    /**
     * 二维码获取服务对象
     */
    private QuickResponseCodeObtainService quickResponseCodeObtainService;

    /**
     * 构造方法
     *
     * @param properties 配置文件对象
     */
    public AuthController(Properties properties, AuthCertificate certificate) {
        this.properties = properties;
        this.certificate = certificate;
    }

    /**
     * 账号密码登录
     */
    @PostMapping("/login/account_password")
    public Mono<ResultContext> accountPasswordLogin(
            ServerWebExchange exchange, @RequestBody AccountPasswordLoginContext.Request param) {
        if (properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable()) {
            if (param == null
                    || param.getAccount() == null
                    || param.getPassword() == null) {
                return Mono.error(GlobalExceptionContext.executeParameterException(
                        this.getClass(),
                        "fun accountPasswordLogin(ServerWebExchange exchange, AccountPasswordLoginContext.Request param)",
                        "Request parameter<account/password> exception."
                ));
            }
            return AuthVoucherContext
                    .init(exchange)
                    .flatMap(v -> accountPasswordLoginService.execute(v, param))
                    .flatMap(o -> certificate.execute(exchange, o))
                    .map(ResultContext::build);
        } else {
            return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                    this.getClass(),
                    "fun accountPasswordLogin(ServerWebExchange exchange, AccountPasswordLoginContext.Request param)",
                    "Account password login service not enabled exception."
            ));
        }
    }

    /**
     * 账号密码登录
     */
    @PostMapping("/login/account_password/signature")
    public Mono<ResultContext> accountPasswordLoginSignature(
            ServerWebExchange exchange, @RequestBody AccountPasswordLoginContext.Signature.Request param) {
        if (properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable()
                && properties.getLogin().getAccountPassword().isEnableTransmissionEncryption()) {
            if (param == null) {
                return Mono.error(new ParameterException(
                        this.getClass(),
                        "fun accountPasswordLoginSignature(ServerWebExchange " +
                                "exchange, AccountPasswordLoginContext.Signature.Request param)",
                        "Request parameter exception."
                ));
            }
            return AuthVoucherContext
                    .init(exchange)
                    .flatMap(v -> accountPasswordLoginSignatureService.execute(v, param))
                    .map(ResultContext::build);
        } else {
            return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                    this.getClass(),
                    "fun accountPasswordLoginSignature(ServerWebExchange " +
                            "exchange, AccountPasswordLoginContext.Signature.Request param)",
                    "Account password signature service not enabled exception."
            ));
        }
    }

    /**
     * 验证码登录
     */
    @PostMapping("/login/verification_code")
    public Mono<ResultContext> verificationCodeLogin(
            ServerWebExchange exchange, @RequestBody VerificationCodeLoginContext.Request param) {
        if (properties.getLogin().isEnable()
                && properties.getLogin().getVerificationCode().isEnable()) {
            if (param == null || param.getCode() == null) {
                return Mono.error(new ParameterException(
                        this.getClass(),
                        "fun verificationCodeLogin(ServerWebExchange " +
                                "exchange, VerificationCodeLoginContext.Request param)",
                        "Request parameter<code> exception."
                ));
            }
            return AuthVoucherContext
                    .init(exchange)
                    .flatMap(v -> verificationCodeLoginService.execute(v, param))
                    .flatMap(o -> certificate.execute(exchange, o))
                    .map(ResultContext::build);
        } else {
            return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                    this.getClass(),
                    "fun verificationCodeLogin(ServerWebExchange " +
                            "exchange, VerificationCodeLoginContext.Request param)",
                    "Verification code login service not enabled exception."
            ));
        }
    }


    @PostMapping("/login/verification_code/obtain")
    public Mono<ResultContext> obtainLoginVerificationCode(
            ServerWebExchange exchange, @RequestBody VerificationCodeLoginContext.Obtain.Request param) {
        if (properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable()) {
            if (param == null || param.getAccount() == null) {
                return Mono.error(new ParameterException(
                        this.getClass(),
                        "fun obtainLoginVerificationCode(ServerWebExchange " +
                                "exchange, VerificationCodeLoginContext.Obtain.Request param)",
                        "Request parameter<account> exception."
                ));
            }
            return AuthVoucherContext
                    .init(exchange)
                    .flatMap(v -> verificationCodeObtainService.execute(v, param))
                    .map(ResultContext::build);
        } else {
            return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                    this.getClass(),
                    "fun obtainLoginVerificationCode(ServerWebExchange " +
                            "exchange, VerificationCodeLoginContext.Obtain.Request param)",
                    "Verification code obtain service not enabled exception."
            ));
        }
    }

    /**
     * 二维码登录
     */
    @PostMapping("/login/quick_response_code")
    public Mono<ResultContext> quickResponseCodeLogin(
            ServerWebExchange exchange, @RequestBody QuickResponseCodeContext.Request param) {
        if (properties.getLogin().isEnable()
                && properties.getLogin().getQrCode().isEnable()) {
            if (param == null) {
                return Mono.error(new ParameterException(
                        this.getClass(),
                        "fun quickResponseCodeLogin(ServerWebExchange " +
                                "exchange, QuickResponseCodeContext.Request param)",
                        "Request parameter exception."
                ));
            }
            return AuthVoucherContext
                    .init(exchange)
                    .flatMap(v -> quickResponseCodeLoginService.execute(v, param))
                    .flatMap(o -> certificate.execute(exchange, o))
                    .map(ResultContext::build);
        } else {
            return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                    this.getClass(),
                    "fun quickResponseCodeLogin(ServerWebExchange " +
                            "exchange, QuickResponseCodeContext.Request param)",
                    "Quick response code login service not enabled exception."
            ));
        }
    }

    @PostMapping("/login/quick_response_code/obtain")
    public Mono<ResultContext> obtainLoginQuickResponseCode(
            ServerWebExchange exchange, @RequestBody QuickResponseCodeContext.Obtain.Request param) {
        if (properties.getLogin().isEnable()
                && properties.getLogin().getQrCode().isEnable()) {
            if (param == null) {
                return Mono.error(new ParameterException(
                        this.getClass(),
                        "fun obtainLoginQuickResponseCode(ServerWebExchange " +
                                "exchange, QuickResponseCodeContext.Obtain.Request param)",
                        "Quick response code parameter exception."
                ));
            }
            return AuthVoucherContext
                    .init(exchange)
                    .flatMap(v -> quickResponseCodeObtainService.execute(v, param))
                    .map(ResultContext::build);
        } else {
            return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                    this.getClass(),
                    "fun obtainLoginQuickResponseCode(ServerWebExchange " +
                            "exchange, QuickResponseCodeContext.Obtain.Request param)",
                    "Quick response code obtain service not enabled exception."
            ));
        }
    }

    /**
     * 设置账号密码登录服务对象
     *
     * @param accountPasswordLoginService 账号密码登录服务对象
     */
    public void setAccountPasswordLoginService(AccountPasswordLoginService accountPasswordLoginService) {
        this.accountPasswordLoginService = accountPasswordLoginService;
    }

    /**
     * 设置账号密码登录签名服务对象
     *
     * @param accountPasswordLoginSignatureService 账号密码登录签名服务对象
     */
    public void setAccountPasswordLoginSignatureService(AccountPasswordLoginSignatureService accountPasswordLoginSignatureService) {
        this.accountPasswordLoginSignatureService = accountPasswordLoginSignatureService;
    }

    /**
     * 设置验证码登录服务对象
     *
     * @param verificationCodeLoginService 验证码登录服务对象
     */
    public void setVerificationCodeLoginService(VerificationCodeLoginService verificationCodeLoginService) {
        this.verificationCodeLoginService = verificationCodeLoginService;
    }

    /**
     * 设置验证码登录服务对象
     *
     * @param verificationCodeObtainService 验证码获取服务对象
     */
    public void setVerificationCodeObtainService(VerificationCodeObtainService verificationCodeObtainService) {
        this.verificationCodeObtainService = verificationCodeObtainService;
    }

    /**
     * 设置二维码登录服务对象
     *
     * @param quickResponseCodeLoginService 二维码登录服务对象
     */
    public void setQuickResponseCodeLoginService(QuickResponseCodeLoginService quickResponseCodeLoginService) {
        this.quickResponseCodeLoginService = quickResponseCodeLoginService;
    }

    /**
     * 设置二维码获取服务对象
     *
     * @param quickResponseCodeObtainService 二维码获取服务对象
     */
    public void setQuickResponseCodeObtainService(QuickResponseCodeObtainService quickResponseCodeObtainService) {
        this.quickResponseCodeObtainService = quickResponseCodeObtainService;
    }

}

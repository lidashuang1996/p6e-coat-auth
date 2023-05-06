package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthForeignMinistry;
import club.p6e.coat.gateway.auth.AuthForeignMinistryVisaTemplate;
import club.p6e.coat.gateway.auth.JsonSerializeDeserializeAuthentication;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.authentication.AccountPasswordAuthenticationVoucherToken;
import club.p6e.coat.gateway.auth.authentication.QuickResponseCodeAuthenticationVoucherToken;
import club.p6e.coat.gateway.auth.authentication.VerificationCodeAuthenticationVoucherToken;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.context.QRCodeLoginContext;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.context.VerificationCodeLoginContext;
import club.p6e.coat.gateway.auth.error.ParameterException;
import club.p6e.coat.gateway.auth.error.ServiceNotEnabledException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final Properties properties;
    private final AuthForeignMinistry authForeignMinistry;
    private final ReactiveAuthenticationManager authenticationManager;

    public AuthController(
            Properties properties,
            AuthForeignMinistry authForeignMinistry,
            ReactiveAuthenticationManager authenticationManager
    ) {
        this.properties = properties;
        this.authForeignMinistry = authForeignMinistry;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public Mono<ResultContext> login(
            @RequestBody LoginContext.Request param,
            ServerRequest request, ServerResponse response
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
                            request, response, AuthForeignMinistryVisaTemplate.create(new JsonSerializeDeserializeAuthentication(authentication))))
                    .map(ResultContext::build);
        } else {
            return Mono.error(new ServiceNotEnabledException(this.getClass(), "", ""));
        }
    }

    @PostMapping("/login/verification_code")
    public Mono<ResultContext> verificationCodeLogin(
            @RequestBody VerificationCodeLoginContext.Request param,
            ServerRequest request, ServerResponse response
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
                            request, response, AuthForeignMinistryVisaTemplate.create(new JsonSerializeDeserializeAuthentication(authentication))))
                    .map(ResultContext::build);
        } else {
            return Mono.error(new ServiceNotEnabledException(this.getClass(), "", ""));
        }
    }

    @PostMapping("/login/quick_response_code")
    public Mono<ResultContext> quickResponseCodeLogin(
            @RequestBody QRCodeLoginContext.Request param,
            ServerRequest request, ServerResponse response
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
                            request, response, AuthForeignMinistryVisaTemplate.create(new JsonSerializeDeserializeAuthentication(authentication))))
                    .map(ResultContext::build);
        } else {
            return Mono.error(new ServiceNotEnabledException(this.getClass(), "", ""));
        }
    }

}

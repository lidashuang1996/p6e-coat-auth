package club.p6e.coat.gateway.auth;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthVoucherContext implements Serializable {

    public static final String PRIVATE = "PRIVATE";
    public static final String INDEX = "INDEX";
    public static final String ACCOUNT_PASSWORD_LOGIN = "ACCOUNT_PASSWORD_LOGIN";
    public static final String ACCOUNT_PASSWORD_CODEC_DATE = "ACCOUNT_PASSWORD_CODEC_DATE";
    public static final String ACCOUNT_PASSWORD_CODEC_MARK = "ACCOUNT_PASSWORD_CODEC_MARK";
    public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String ACCOUNT = "ACCOUNT";
    public static final String USER_NAME = "USER_NAME";
    public static final String VERIFICATION_CODE_LOGIN_DATE = "VERIFICATION_CODE_LOGIN_DATE";
    public static final String VERIFICATION_CODE_LOGIN_MARK = "VERIFICATION_CODE_LOGIN_MARK";
    public static final String QUICK_RESPONSE_CODE_LOGIN_MARK = "QUICK_RESPONSE_CODE_LOGIN_MARK";
    public static final String QUICK_RESPONSE_CODE_LOGIN_DATE = "QUICK_RESPONSE_CODE_LOGIN_DATE";

    public static final String OAUTH2 = "OAUTH2";
    public static final String OAUTH2_STATE = "OAUTH2_STATE";
    public static final String OAUTH2_SCOPE = "OAUTH2_SCOPE";
    public static final String OAUTH2_CLIENT_ID = "OAUTH2_CLIENT_ID";
    public static final String OAUTH2_REDIRECT_URI = "OAUTH2_REDIRECT_URI";
    public static final String OAUTH2_RESPONSE_TYPE = "OAUTH2_RESPONSE_TYPE";
    public static final String OAUTH2_CLIENT_RECONFIRM = "OAUTH2_CLIENT_RECONFIRM";
    public static final String OAUTH2_CLIENT_NAME = "OAUTH2_CLIENT_NAME";
    public static final String OAUTH2_CLIENT_AVATAR = "OAUTH2_CLIENT_AVATAR";
    public static final String OAUTH2_CLIENT_DESCRIBE = "OAUTH2_CLIENT_DESCRIBE";
    public static final String OAUTH2_CODE = "OAUTH2_CODE";
    public static final String OAUTH2_USER_ID = "OAUTH2_USER_ID";
    public static final String OAUTH2_USER_INFO = "OAUTH2_USER_INFO";

    public static Mono<AuthVoucherContext> init(ServerWebExchange exchange) {
        return Mono.just(new AuthVoucherContext());
    }

    public static Mono<AuthVoucherContext> create() {
        return Mono.just(new AuthVoucherContext());
    }

    private String mark;

    public String getMark() {
        return mark;
    }

    public Mono<AuthVoucherContext> set(Map<String, String> map) {
        return Mono.just(new AuthVoucherContext());
    }

    public String get(String accountPasswordCodecMark) {
        return "";
    }
}

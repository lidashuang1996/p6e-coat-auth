package club.p6e.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Component("club.p6e.coat.gateway.auth.Properties")
@ConfigurationProperties(prefix = "club.p6e.cloud.gateway.auth")
public class Properties {

    public static final String AUTH_PROPERTIES_PREFIX = "club.p6e.cloud.gateway.auth";

    private Mode mode = Mode.PHONE_OR_MAILBOX;


    private boolean enable = true;
    private boolean redirectIndexPage = true;
    private String redirectIndexPagePath = "/";

    private Login login = new Login();
    private Oauth2 oauth2 = new Oauth2();
    private Register register = new Register();

    private Cache cache = new Cache();

    private Auth auth = new Auth();

    private Redis redis = new Redis();
    private DataSource dataSource = new DataSource();

    /**
     * Referer
     */
    private Referer referer = new Referer();

    /**
     * 跨域
     */
    private CrossDomain crossDomain = new CrossDomain();


    /**
     * Referer
     */
    @Data
    @Accessors(chain = true)
    public static class Referer {

        /**
         * 是否启动
         */
        private boolean enabled = false;

        /**
         * 白名单
         */
        private String[] whiteList = new String[]{"*"};

    }

    /**
     * 跨域
     */
    @Data
    @Accessors(chain = true)
    public static class CrossDomain {

        /**
         * 是否启动
         */
        private boolean enabled = false;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Redis extends RedisProperties {
        private Map<String, RedisProperties> nodes = new HashMap<>();
    }


    @Data
    public static class Cache {
        private Type type = Type.MEMORY;

        public enum Type {
            REDIS,
            MEMORY
        }
    }

    @Data
    public static class DataSource {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }

    public enum Mode {
        PHONE,
        MAILBOX,
        ACCOUNT,
        PHONE_OR_MAILBOX;


        public static Mode create(String mode) {
            return switch (mode) {
                case "PHONE" -> PHONE;
                case "MAILBOX" -> MAILBOX;
                case "ACCOUNT" -> ACCOUNT;
                default -> PHONE_OR_MAILBOX;
            };
        }
    }

    @Data
    public static class Login {
        private boolean enable = true;
        private AccountPassword accountPassword = new AccountPassword();
        private VerificationCode verificationCode = new VerificationCode();
        private QrCode qrCode = new QrCode();
        private Map<String, Other> others = new HashMap<>();

        public Login() {
            final Map<String, String> map = new HashMap<>();
            map.put(
                    "home",
                    "https://graph.qq.com/oauth2.0/authorize"
                            + "?response_type=@{home_response_type}"
                            + "&client_id=@{home_client_id}"
                            + "&redirect_uri=@{home_redirect_uri}"
                            + "&scope=@{home_scope}"
            );
            map.put("home_scope", "get_user_info");
            map.put("home_response_type", "code");
            map.put("home_client_id", "101701115");
            map.put("home_redirect_uri", "http://www.zhimamimi.com/login/qq/callback");

            map.put(
                    "token",
                    "https://graph.qq.com/oauth2.0/token"
                            + "?grant_type=@{token_grant_type}"
                            + "&client_id=@{token_client_id}"
                            + "&client_secret=@{token_client_secret}"
                            + "&redirect_uri=@{token_redirect_uri}"
            );
            map.put("token_grant_type", "authorization_code");
            map.put("token_client_id", "101701115");
            map.put("token_client_secret", "165716bf22444998e0394d0e244e13d7");
            map.put("token_redirect_uri", "http://www.zhimamimi.com/login/qq/callback");
            map.put("me", "https://graph.qq.com/oauth2.0/me");
            map.put("info", "https://graph.qq.com/user/get_user_info?oauth_consumer_key=@{info_oauth_consumer_key}");
            map.put("info_oauth_consumer_key", "101701115");
            final Other qq = new Other();
            qq.setEnable(true);
            qq.setConfig(map);
            others.put("QQ", qq);
        }

        @Data
        public static class AccountPassword {
            private boolean enable = true;
            private boolean enableTransmissionEncryption = false;
        }

        @Data
        public static class VerificationCode {
            private boolean enable = false;
        }

        @Data
        public static class QrCode {
            private boolean enable = false;
        }

        @Data
        public static class Other {
            private boolean enable = false;
            private Map<String, String> config = new HashMap<>();
        }
    }

    @Data
    public static class Oauth2 {
        private boolean enable = true;
        private Client client = new Client();
        private Password password = new Password();
        private AuthorizationCode authorizationCode = new AuthorizationCode();

        @Data
        public static class AuthorizationCode {
            private boolean enable = true;
        }

        @Data
        public static class Password {
            private boolean enable = true;
        }

        @Data
        public static class Client {
            private boolean enable = true;
        }
    }

    @Data
    public static class Register {
        private boolean enable = false;
    }

    @Data
    public static class Auth {
        private Bean validator = new Bean("club.p6e.auth.certificate." +
                "HttpLocalStorageJsonWebTokenCertificateValidator", new String[]{
                "club.p6e.auth.AuthJsonWebTokenCipher"
        });
        private Bean authority = new Bean("club.p6e.auth.certificate." +
                "HttpLocalStorageJsonWebTokenCertificateAuthority", new String[]{
                "club.p6e.auth.AuthJsonWebTokenCipher"
        });
    }


    @Data
    public static class Bean {
        private String name;
        private String[] depend = new String[]{};

        public Bean(String name) {
            this.name = name;
        }

        public Bean(String name, String[] depend) {
            this.name = name;
            this.depend = depend;
        }
    }

}

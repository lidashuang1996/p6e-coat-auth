package club.p6e.coat.gateway.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
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

    private Login login = new Login();
    private Oauth2 oauth2 = new Oauth2();
    private Register register = new Register();

    private Cache cache = new Cache();

    private Auth auth = new Auth();

    private Redis redis = new Redis();
    private DataSource dataSource = new DataSource();

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
        private Other other = new Other();

        @Data
        public static class AccountPassword {
            private boolean enable = true;
            private boolean enableTransmissionEncryption = true;
        }

        @Data
        public static class VerificationCode {
            private boolean enable = true;
        }

        @Data
        public static class QrCode {
            private boolean enable = true;
        }

        @Data
        public static class Other {
            private boolean enable = false;
            private Map<String, Map<String, String>> config = new HashMap<>();
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
            private boolean enable = false;
        }

        @Data
        public static class Password {
            private boolean enable = false;
        }

        @Data
        public static class Client {
            private boolean enable = false;
        }
    }

    @Data
    public static class Register {
        private boolean enable = false;
    }

    @Data
    public static class Auth {
        private Bean validator = new Bean("club.p6e.coat.gateway.auth.certificate." +
                "AuthCertificateInterceptorHttpLocalStorageJsonWebToken", new String[]{
                "club.p6e.coat.gateway.auth.AuthJsonWebTokenCipher"
        });
        private Bean authority = new Bean("club.p6e.coat.gateway.auth.certificate." +
                "AuthCertificateAuthorityHttpLocalStorageJsonWebTokenImpl", new String[]{
                "club.p6e.coat.gateway.auth.AuthJsonWebTokenCipher"
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

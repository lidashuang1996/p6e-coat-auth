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
@Component
@ConfigurationProperties(prefix = "p6e.auth")
public class Properties {

    private Mode mode = Mode.ACCOUNT;

    private Login login = new Login();
    private Oauth2 oauth2 = new Oauth2();
    private Register register = new Register();

    private Redis redis = new Redis();
    private DataSource dataSource = new DataSource();

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Redis extends RedisProperties {
        private Map<String, RedisProperties> nodes = new HashMap<>();
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
        PHONE_OR_MAILBOX
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
            private boolean enable = false;
        }

        @Data
        public static class QrCode {
            private boolean enable = false;
        }

        @Data
        public static class Other {
            private boolean enable = false;
            private Map<String, Map<String, String>> config = new HashMap<>();
        }
    }

    @Data
    public static class Oauth2 {
        private boolean enable = false;
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


}

package club.p6e.cloud.auth.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * OAUTH2 的上下文对象
 *
 * @author lidashuang
 * @version 1.0
 */
public class InfoContext implements Serializable {

    /**
     * OAUTH2 的请求对象
     */
    @Data
    @Accessors(chain = true)
    public static class Request implements Serializable {

        /**
         * 凭证
         */
        private String voucher;

        /**
         * 凭证读取的内容
         */
        private Map<String, String> voucherMap = new HashMap<>();

    }

    /**
     * 认证对象
     */
    public static class Auth implements Serializable {

        /**
         * 请求对象
         */
        @Data
        @Accessors(chain = true)
        public static class Request implements Serializable {
            private String responseType;
            private String clientId;
            private String redirectUri;
            private String scope;
            private String state;
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private String voucher;
        }

    }

    /**
     * 令牌对象
     */
    public static class Token implements Serializable {

        /**
         * 请求对象
         */
        @Data
        @Accessors(chain = true)
        public static class Request implements Serializable {
            private String grantType;
            private String clientId;
            private String clientSecret;
            private String username;
            private String password;
            private String code;
            private String redirectUri;
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private String id;
            private String openId;
            private String accessToken;
            private String refreshToken;
            private Long expiration;
        }

    }

    /**
     * 二次确认对象
     */
    public static class Confirm implements Serializable {

        /**
         * 请求对象
         */
        @Data
        @Accessors(chain = true)
        @EqualsAndHashCode(callSuper = true)
        public static class Request extends InfoContext.Request implements Serializable {
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private String code;
            private String state;
            private String redirectUri;
        }

    }

}

package club.p6e.coat.gateway.auth.context;

import lombok.Data;
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
public class RegisterContext implements Serializable {

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

        private String account;
        private String password;
        private String ol;

    }

    @Data
    @Accessors(chain = true)
    public static class Dto implements Serializable {
        private String code;
        private String state;
        private String redirectUri;
    }

    public class Verification implements Serializable {

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
            private String ol;

        }

        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private String ol;
        }



    }

}

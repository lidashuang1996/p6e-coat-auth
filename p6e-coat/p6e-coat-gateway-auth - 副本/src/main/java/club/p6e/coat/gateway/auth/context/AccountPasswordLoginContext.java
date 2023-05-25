package club.p6e.coat.gateway.auth.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 账号密码/登录上下文对象
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AccountPasswordLoginContext implements Serializable {

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Request extends VoucherContext.Request {

        /**
         * 账号
         */
        private String account;

        /**
         * 密码
         */
        private String password;

    }

    @Data
    @Accessors(chain = true)
    public static class Signature implements Serializable {

        @Data
        public static class Request {
        }

        @Data
        public static class Dto {
            private String key;
        }

    }


}

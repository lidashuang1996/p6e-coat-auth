package club.p6e.coat.gateway.auth.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录的上下文对象
 *
 * @author lidashuang
 * @version 1.0
 */
public class LoginContext implements Serializable {

    /**
     * 登录的请求对象
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
     * 验证登录的对象
     */
    public static class Verification implements Serializable {

        /**
         * 请求对象
         */
        @Data
        @Accessors(chain = true)
        public static class Request implements Serializable {
        }

    }

    /**
     * 账号密码登录的对象
     */
    public static class AccountPassword implements Serializable {

        /**
         * 请求对象
         */
        @Data
        @Accessors(chain = true)
        @EqualsAndHashCode(callSuper = true)
        public static class Request extends LoginContext.Request implements Serializable {
            private String account;
            private String password;
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private Integer id;
            private String account;
            private Integer status;
            private String name;
            private String nickname;
            private String avatar;
            private String describe;
        }

    }

    /**
     * 账号密码签名的对象
     */
    public static class AccountPasswordSignature implements Serializable {

        /**
         * 请求对象
         */
        @Data
        @Accessors(chain = true)
        public static class Request implements Serializable {
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private String content;
        }

    }

    /**
     * 验证码登录的对象
     */
    public static class VerificationCode implements Serializable {

        /**
         * 请求对象
         */
        @Data
        @Accessors(chain = true)
        public static class Request implements Serializable {
            private String code;
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private Integer id;
            private String account;
            private Integer status;
            private String name;
            private String nickname;
            private String avatar;
            private String describe;
        }

    }

    /**
     * 验证码获取的对象
     */
    public static class VerificationCodeObtain implements Serializable {

        /**
         * 请求对象
         */
        @Data
        @Accessors(chain = true)
        public static class Request implements Serializable {
            private String account;
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Vo implements Serializable {
            private String account;
            private String message;
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private String account;
            private String message;
        }

    }

    /**
     * 二维码登录对象
     */
    public static class QrCode implements Serializable {

        /**
         * 请求对象
         */
        @Data
        @Accessors(chain = true)
        @EqualsAndHashCode(callSuper = true)
        public static class Request extends LoginContext.Request implements Serializable {
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private Integer id;
            private String account;
            private Integer status;
            private String name;
            private String nickname;
            private String avatar;
            private String describe;
        }

    }

    /**
     * 二维码获取对象
     */
    public static class QrCodeObtain implements Serializable {

        /**
         * 请求对象
         */
        @Data
        @Accessors(chain = true)
        @EqualsAndHashCode(callSuper = true)
        public static class Request extends LoginContext.Request implements Serializable {
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Vo implements Serializable {
            private String content;
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private String content;
        }

    }

    /**
     * 二维码回调对象
     */
    public static class QrCodeCallback implements Serializable {

        /**
         * 请求对象
         */
        @Data
        @Accessors(chain = true)
        public static class Request implements Serializable {
            private String content;
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Vo implements Serializable {
            private String content;
        }

        /**
         * 结果对象
         */
        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private String content;
        }

    }

    /**
     * 第三方登录对象
     */
    public static class Other implements Serializable {

        /**
         * 第三方登录 QQ 对象
         */
        public static class Qq implements Serializable {

            /**
             * 请求对象
             */
            @Data
            @Accessors(chain = true)
            public static class Request implements Serializable {
                private String code;
                private String state;
            }

            /**
             * 结果对象
             */
            @Data
            @Accessors(chain = true)
            public static class Dto implements Serializable {
                private Integer id;
                private String account;
                private Integer status;
                private String name;
                private String nickname;
                private String avatar;
                private String describe;
            }

        }
    }

}

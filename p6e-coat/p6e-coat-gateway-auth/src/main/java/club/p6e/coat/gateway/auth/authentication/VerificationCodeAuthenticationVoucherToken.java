package club.p6e.coat.gateway.auth.authentication;

import org.springframework.security.core.CredentialsContainer;

/**
 * 验证码认证
 * 是对抽象的认证凭证令牌对象的扩展
 *
 * @author lidashuang
 * @version 1.0
 */
public class VerificationCodeAuthenticationVoucherToken extends AbstractAuthenticationVoucherToken {

    /**
     * 创建验证码认证对象
     *
     * @param voucher 凭证
     * @param key     验证码标记
     * @param value   验证码内容
     * @return 验证码认证对象
     */
    public static VerificationCodeAuthenticationVoucherToken create(String voucher, String key, String value) {
        return new VerificationCodeAuthenticationVoucherToken(voucher, key, value);
    }

    /**
     * 构造方法初始化
     *
     * @param voucher 凭证
     * @param key     验证码标记
     * @param value   验证码内容
     */
    public VerificationCodeAuthenticationVoucherToken(String voucher, String key, String value) {
        super(
                voucher,
                new VerificationCodeAuthenticationVoucherToken.KeyValueCredentialsContainer(key),
                new VerificationCodeAuthenticationVoucherToken.KeyValueCredentialsContainer(value)
        );
    }

    /**
     * 验证码包装对象
     */
    public static class KeyValueCredentialsContainer implements CredentialsContainer {

        /**
         * 数据内容
         */
        private String content;

        /**
         * 构造方法初始化
         *
         * @param content 数据内容
         */
        public KeyValueCredentialsContainer(String content) {
            this.content = content;
        }

        /**
         * 清除敏感数据
         */
        @Override
        public void eraseCredentials() {
            content = null;
        }

        /**
         * 输出数据内容字符串格式内容
         *
         * @return 数据内容字符串格式内容
         */
        @Override
        public String toString() {
            return content;
        }

    }

}

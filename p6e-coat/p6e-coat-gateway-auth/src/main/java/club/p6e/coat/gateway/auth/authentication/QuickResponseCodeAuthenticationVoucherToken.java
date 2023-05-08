package club.p6e.coat.gateway.auth.authentication;

import org.springframework.security.core.CredentialsContainer;

/**
 * 二维码认证
 * 是对抽象的认证凭证令牌对象的扩展
 *
 * @author lidashuang
 * @version 1.0
 */
public class QuickResponseCodeAuthenticationVoucherToken extends AbstractAuthenticationVoucherToken {

    /**
     * 创建二维码认证对象
     *
     * @param voucher 凭证
     * @param content 二维码内容
     * @return 二维码认证对象
     */
    public static QuickResponseCodeAuthenticationVoucherToken create(String voucher, String content) {
        return new QuickResponseCodeAuthenticationVoucherToken(voucher, content);
    }

    /**
     * 构造方法初始化
     *
     * @param voucher 凭证
     * @param content 二维码内容
     */
    public QuickResponseCodeAuthenticationVoucherToken(String voucher, String content) {
        super(voucher, null, new QuickResponseCodeAuthenticationVoucherToken.ContentCredentialsContainer(content));
    }

    /**
     * 构造方法初始化
     *
     * @param voucher     凭证
     * @param principal   无意义
     * @param credentials 二维码内容
     */
    public QuickResponseCodeAuthenticationVoucherToken(String voucher, Object principal, Object credentials) {
        super(voucher, principal, credentials);
    }

    /**
     * 二维码包装对象
     */
    public static class ContentCredentialsContainer implements CredentialsContainer {

        /**
         * 数据内容
         */
        private String content;

        /**
         * 构造方法初始化
         *
         * @param content 数据内容
         */
        public ContentCredentialsContainer(String content) {
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

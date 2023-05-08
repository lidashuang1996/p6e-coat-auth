package club.p6e.coat.gateway.auth.authentication;

import org.springframework.security.core.CredentialsContainer;

/**
 * 账号密码认证
 * 是对抽象的认证凭证令牌对象的扩展
 *
 * @author lidashuang
 * @version 1.0
 */
public class AccountPasswordAuthenticationVoucherToken extends AbstractAuthenticationVoucherToken {

    /**
     * 创建账号密码认证对象
     *
     * @param voucher  凭证
     * @param account  账号
     * @param password 密码
     * @return 账号密码认证对象
     */
    public static AccountPasswordAuthenticationVoucherToken create(
            String voucher, String account, String password
    ) {
        return new AccountPasswordAuthenticationVoucherToken(voucher, account, password);
    }

    /**
     * 构造方法初始化
     *
     * @param voucher  凭证
     * @param account  账号
     * @param password 密码
     */
    public AccountPasswordAuthenticationVoucherToken(String voucher, String account, String password) {
        super(voucher, account, new PasswordCredentialsContainer(password));
    }

    /**
     * 密码包装对象
     */
    public static class PasswordCredentialsContainer implements CredentialsContainer {

        /**
         * 数据内容
         */
        private String content;

        /**
         * 构造方法初始化
         *
         * @param content 数据内容
         */
        public PasswordCredentialsContainer(String content) {
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

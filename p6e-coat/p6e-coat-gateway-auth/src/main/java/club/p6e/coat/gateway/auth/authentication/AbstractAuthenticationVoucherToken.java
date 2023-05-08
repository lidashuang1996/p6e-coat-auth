package club.p6e.coat.gateway.auth.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 抽象的认证凭证令牌对象
 *
 * @author lidashuang
 * @version 1.0
 */
public class AbstractAuthenticationVoucherToken extends AbstractAuthenticationToken {

    /**
     * 凭证
     */
    private final String voucher;

    /**
     * 账号
     */
    private final Object principal;

    /**
     * 密码
     */
    private final Object credentials;

    /**
     * 构造方法初始化
     *
     * @param voucher     凭证
     * @param principal   账号
     * @param credentials 密码
     */
    public AbstractAuthenticationVoucherToken(String voucher, Object principal, Object credentials) {
        // 不使用内置的方法进行权限验证
        super(null);
        this.voucher = voucher;
        this.principal = principal;
        this.credentials = credentials;
        // 用于获取授权时候使用
        // 这里设置认证变量固定为未认证
        setAuthenticated(false);
    }

    /**
     * 获取账号
     *
     * @return 账号数据
     */
    @Override
    public Object getPrincipal() {
        return principal;
    }

    /**
     * 获取密码
     *
     * @return 密码数据
     */
    @Override
    public Object getCredentials() {
        return credentials;
    }

    /**
     * 获取凭证
     *
     * @return 凭证数据
     */
    public String getVoucher() {
        return voucher;
    }

}

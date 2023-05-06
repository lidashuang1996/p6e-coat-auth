package club.p6e.coat.gateway.auth;

/**
 * 生成令牌客户端认证令牌
 *
 * @author lidashuang
 * @version 1.0
 */
public interface AuthRefreshTokenGenerator {

    /**
     * 生成令牌
     *
     * @return 令牌
     */
    public String execute();

}

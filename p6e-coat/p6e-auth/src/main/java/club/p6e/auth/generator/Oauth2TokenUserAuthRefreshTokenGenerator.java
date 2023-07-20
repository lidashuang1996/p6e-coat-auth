package club.p6e.auth.generator;

/**
 * 生成用户认证令牌
 *
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2TokenUserAuthRefreshTokenGenerator {

    /**
     * 生成令牌
     *
     * @return 令牌
     */
    public String execute();

}

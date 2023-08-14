package club.p6e.auth.generator;

/**
 * 生成用户认证令牌
 *
 * @author lidashuang
 * @version 1.0
 */
public interface OAuth2TokenUserAuthAccessTokenGenerator {

    /**
     * 生成令牌
     *
     * @return 令牌
     */
    public String execute();

}

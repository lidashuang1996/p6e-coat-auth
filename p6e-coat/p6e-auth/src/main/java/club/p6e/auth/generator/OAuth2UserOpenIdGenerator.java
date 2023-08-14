package club.p6e.auth.generator;

/**
 * 生成用户 OPENID 数据
 *
 * @author lidashuang
 * @version 1.0
 */
public interface OAuth2UserOpenIdGenerator {

    public String execute(String clientId, String userId);

}

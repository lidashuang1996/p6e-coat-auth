package club.p6e.auth.generator;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2UserOpenIdGenerator {

    public String execute(String clientId, String userId);

}

package club.p6e.coat.gateway.auth.generator;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2UserOpenIdGenerator {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION = "#{${p6e.auth.oauth2.enable:false}}";

    public String execute(String clientId, String userId);

}

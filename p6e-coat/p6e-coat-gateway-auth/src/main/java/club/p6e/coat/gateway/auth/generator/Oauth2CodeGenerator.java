package club.p6e.coat.gateway.auth.generator;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2CodeGenerator {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.oauth2.enable:false} && ${p6e.auth.oauth2.authorization-code.enable:false}}";

    public String execute();
}

package club.p6e.cloud.auth.generator;

/**
 * 验证码登录
 * 验证码生成器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface CodeLoginGenerator {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.verification-code.enable:false}}";

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    public String execute(String type);
}

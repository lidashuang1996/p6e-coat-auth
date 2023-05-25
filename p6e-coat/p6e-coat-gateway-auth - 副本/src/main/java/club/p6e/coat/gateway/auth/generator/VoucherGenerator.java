package club.p6e.coat.gateway.auth.generator;

/**
 * 凭证会话
 * 凭证会话序号生成器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface VoucherGenerator {

    /**
     * 条件注册的条件表达式
     */
    public static final String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} || ${p6e.auth.oauth2.enable:false} || ${p6e.auth.register.enable:false}}";

    /**
     * 生成会话序号
     *
     * @return 会话序号
     */
    public String execute();

}

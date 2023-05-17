package club.p6e.coat.gateway.auth.launcher;

import java.util.Map;

/**
 * 消息推送器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface MessageLauncher {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION = "#{${p6e.auth.register.enable:false} " +
            " || (${p6e.auth.login.enable:false} && ${p6e.auth.login.verification-code.enable:false})}";

    /**
     * 推送器类型
     *
     * @return 类型
     */
    public String toType();

    /**
     * 执行推送
     *
     * @param account  账号
     * @param template 模版名称
     * @param content  内容
     * @return 推送消息的返回
     */
    public String execute(String account, String template, Map<String, String> content);

}

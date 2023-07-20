package club.p6e.cloud.auth.launcher;

import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 消息推送器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface MessageLauncher {

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
    public Mono<String> execute(String account, String template, Map<String, String> content);

}

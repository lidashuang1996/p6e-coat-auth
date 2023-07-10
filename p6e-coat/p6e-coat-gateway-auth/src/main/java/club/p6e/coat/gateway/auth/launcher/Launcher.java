package club.p6e.coat.gateway.auth.launcher;

import club.p6e.coat.gateway.auth.utils.SpringUtil;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 消息推送器
 *
 * @author lidashuang
 * @version 1.0
 */
public final class Launcher {

    /**
     * 推送消息
     *
     * @param type     类型
     * @param account  账号
     * @param template 模版名称
     * @param content  内容
     * @return 推送的结果返回
     */
    public static Mono<String> push(LauncherType type, String account, String template, Map<String, String> content) {
        if (LauncherType.SMS == type) {
            return SpringUtil.getBean(SmsMessageLauncher.class).execute(account, template, content);
        }
        if (LauncherType.EMAIL == type) {
            return SpringUtil.getBean(EmailMessageLauncher.class).execute(account, template, content);
        }
        return Mono.error(new RuntimeException());
    }
}

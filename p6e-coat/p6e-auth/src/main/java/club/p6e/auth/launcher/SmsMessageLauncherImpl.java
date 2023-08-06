package club.p6e.auth.launcher;

import club.p6e.auth.utils.GeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 短信消息推送器的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class SmsMessageLauncherImpl implements SmsMessageLauncher {

    /**
     * 注入日志对象
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(SmsMessageLauncherImpl.class);

    @Override
    public Mono<String> execute(String account, String template, Map<String, String> content) {
        final String result = GeneratorUtil.uuid();
        LOGGER.info("\r\n" +
                "\n" +
                "---------------------------------------------------------------------\n" +
                "|--------------------------   S   M   S   --------------------------|\n" +
                "---------------------------------------------------------------------\n" +
                "|   account   =>   " + account + "   |\n" +
                "|   template   =>   " + template + "   |\n" +
                "|   content   =>   " + content + "   |\n" +
                "--------------------------------------------------------------------\n" +
                "\n");
        return Mono.just(result);
    }

}

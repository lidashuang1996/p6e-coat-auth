package club.p6e.coat.gateway.permission;

import club.p6e.coat.gateway.permission.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

/**
 * 事件监听
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class ApplicationListener implements
        org.springframework.context.ApplicationListener<ApplicationReadyEvent> {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationListener.class);

    /**
     * Spring Boot 初始化完成事件监听
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 初始化上下文对象
        SpringUtil.init(event.getApplicationContext());
        // 打印配置文件的信息
        final Properties properties = SpringUtil.getBean(Properties.class);
        LOGGER.info("=============================================================");
        LOGGER.info("--------- " + Properties.class + " ---------");
        final Properties.Task task = properties.getTask();
        LOGGER.info("task:");
        LOGGER.info("   interval:" + task.getInterval());
        LOGGER.info("--------- " + Properties.class + " ---------");
        LOGGER.info("=============================================================");
        LOGGER.info("Initialization completed.");
    }

}

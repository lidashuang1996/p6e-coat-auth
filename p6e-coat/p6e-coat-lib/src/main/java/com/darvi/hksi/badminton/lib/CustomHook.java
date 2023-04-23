package com.darvi.hksi.badminton.lib;

import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import com.darvi.hksi.badminton.lib.context.WebResultErrorContent;
import com.darvi.hksi.badminton.lib.utils.SnowflakeIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Spring Boot 初始化之后钩子函数
 *
 * @author lidashuang
 * @version 1.0
 */
@Component(CustomHook.BEAN_NAME)
public class CustomHook implements ApplicationRunner {

    /**
     * 注入的 BEAN 的名称
     */
    public static final String BEAN_NAME = "com.darvi.hksi.badminton.lib.CustomHook";

    /**
     * 注入日志对象
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomHook.class);

    /**
     * 上下问对象
     */
    private final ApplicationContext application;

    /**
     * 构造方法初始化上下文对象
     */
    public CustomHook(ApplicationContext application) {
        this.application = application;
    }

    @Override
    public void run(ApplicationArguments args) {
        SpringUtil.init(application);
        WebResultErrorContent.init();
        final Properties properties = application.getBean(Properties.class);
        for (final String name : properties.getSnowflake().keySet()) {
            final Properties.Snowflake snowflake = properties.getSnowflake().get(name);
            SnowflakeIdUtil.register(name, snowflake.getWorkerId(), snowflake.getDatacenterId());
            LOGGER.info("smart construction application initialization snowflake [ worker_id: "
                    + snowflake.getWorkerId() + ", datacenter_id: " + snowflake.getDatacenterId() + "] ==> " + name);
        }
        LOGGER.info("smart construction application initialization complete ...");
    }

}

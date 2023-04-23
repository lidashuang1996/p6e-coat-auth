package com.darvi.hksi.badminton.lib;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Component(value = Properties.BEAN_NAME)
@ConfigurationProperties(prefix = "hksi.badminton.lib")
public class Properties {

    /**
     * 注入的 BEAN 的名称
     */
    public static final String BEAN_NAME = "com.darvi.hksi.badminton.lib.Properties";

    /**
     * 版本号
     */
    private String version = "unknown";

    /**
     * 安全
     */
    private Security security = new Security();

    /**
     * 雪花
     */
    private Map<String, Snowflake> snowflake = new HashMap<>();

    @Data
    public static class Security {

        /**
         * 是否启用
         */
        private boolean enable = false;

        /**
         * 凭证
         */
        private String[] vouchers = new String[]{};
    }

    @Data
    public static class Snowflake {
        private Integer workerId;
        private Integer datacenterId;
    }
}

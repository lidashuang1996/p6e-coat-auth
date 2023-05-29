package club.p6e.coat.gateway;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 配置文件
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Component
@Accessors(chain = true)
@ConfigurationProperties(prefix = "club.p6e.coat.gateway")
public class Properties implements Serializable {

    /**
     * 版本号
     */
    private String version = "20240101@V1.0";

    /**
     * Referer
     */
    private Referer referer = new Referer();

    /**
     * 跨域
     */
    private CrossDomain crossDomain = new CrossDomain();

    /**
     * Referer
     */
    @Data
    @Accessors(chain = true)
    public static class Referer {

        /**
         * 是否启动
         */
        private boolean enabled = false;

        /**
         * 白名单
         */
        private String[] whiteList = new String[]{"*"};

    }

    /**
     * 跨域
     */
    @Data
    @Accessors(chain = true)
    public static class CrossDomain {

        /**
         * 是否启动
         */
        private boolean enabled = false;

    }
}
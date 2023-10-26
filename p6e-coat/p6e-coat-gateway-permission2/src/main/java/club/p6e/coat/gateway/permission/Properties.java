package club.p6e.coat.gateway.permission;

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
@ConfigurationProperties(prefix = "p6e.coat.gateway.permission")
public class Properties implements Serializable {

    /**
     * 定时任务配置
     */
    private Task task = new Task();

    @Data
    @Accessors(chain = true)
    public static class Task implements Serializable {
        private long interval = 3 * 3600 * 1000L;
    }

}
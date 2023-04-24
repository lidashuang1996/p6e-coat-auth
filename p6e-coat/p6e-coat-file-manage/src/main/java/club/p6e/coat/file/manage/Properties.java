package club.p6e.coat.file.manage;

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
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "hksi.bicycle.file")
public class Properties implements Serializable {

    private String[] suffixes = new String[]{"mp4"};
    private String uploadBasePath = "D://aaa";

}
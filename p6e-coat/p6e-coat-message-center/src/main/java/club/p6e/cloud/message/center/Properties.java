package club.p6e.cloud.message.center;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "p6e.cloud.index")
public class Properties implements Serializable {

    private Auth auth = new Auth();
    private FileUpload fileUpload = new FileUpload();

    @Data
    public static class Auth implements Serializable {
        private String url;
    }

    @Data
    public static class FileUpload implements Serializable {
        private String path;
    }

}

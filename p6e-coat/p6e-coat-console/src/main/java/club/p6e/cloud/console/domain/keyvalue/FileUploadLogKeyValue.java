package club.p6e.cloud.console.domain.keyvalue;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
public class FileUploadLogKeyValue implements Serializable {

    @Data
    @Accessors(chain = true)
    public static class Storage implements Serializable {
        private Integer files;
        private Integer folders;
        private Long memory;
    }

}

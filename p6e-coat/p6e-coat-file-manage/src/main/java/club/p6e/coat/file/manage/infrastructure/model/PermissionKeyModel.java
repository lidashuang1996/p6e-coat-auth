package club.p6e.coat.file.manage.infrastructure.model;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class PermissionKeyModel implements Serializable {
    private Integer fid;
    private String type;
    private Integer content;
}

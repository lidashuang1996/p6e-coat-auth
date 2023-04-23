package club.p6e.coat.gateway.permission.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class PermissionUrlGroupRelationUrlModel {

    private Integer gid;
    private Integer uid;
    private String config;
    private String attribute;

}

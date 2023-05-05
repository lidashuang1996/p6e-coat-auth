package club.p6e.coat.gateway.permission.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 权限模型
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class PermissionModel implements Serializable {

    private Integer uid;
    private Integer gid;
    private String uUrl;
    private String uBaseUrl;
    private String uMethod;
    private String uName;
    private String uConfig;
    private Integer gWeight;
    private String gMark;
    private String gName;
    private String rConfig;
    private String rAttribute;

}

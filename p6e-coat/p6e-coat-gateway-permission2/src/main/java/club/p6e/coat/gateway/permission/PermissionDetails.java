package club.p6e.coat.gateway.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 权限详情
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class PermissionDetails implements Serializable {

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

    public PermissionDetails copy() {
        final PermissionDetails result = new PermissionDetails();
        result.setUid(uid);
        result.setGid(gid);
        result.setUUrl(uUrl);
        result.setUBaseUrl(uBaseUrl);
        result.setUMethod(uMethod);
        result.setUName(uName);
        result.setUConfig(uConfig);
        result.setGWeight(gWeight);
        result.setGMark(gMark);
        result.setGName(gName);
        result.setRConfig(rConfig);
        result.setRAttribute(rAttribute);
        return result;
    }
}

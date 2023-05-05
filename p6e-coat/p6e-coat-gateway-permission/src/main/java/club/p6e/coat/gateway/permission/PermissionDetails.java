package club.p6e.coat.gateway.permission;

import club.p6e.coat.gateway.permission.model.PermissionModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
public class PermissionDetails extends PermissionModel implements Serializable {

    /**
     * 构造方法初始化
     *
     * @param permission 权限模型对象
     */
    public PermissionDetails(PermissionModel permission) {
        this.setUid(permission.getUid());
        this.setGid(permission.getUid());
        this.setUUrl(permission.getUUrl());
        this.setUBaseUrl(permission.getUBaseUrl());
        this.setUMethod(permission.getUMethod());
        this.setUName(permission.getUName());
        this.setUConfig(permission.getUConfig());
        this.setGMark(permission.getGMark());
        this.setGName(permission.getGName());
        this.setGWeight(permission.getGWeight());
        this.setRConfig(permission.getRConfig());
        this.setRAttribute(permission.getRAttribute());
    }

}

package club.p6e.coat.gateway.permission.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class P6ePermissionModel implements Serializable {
    
    private Integer id;
    private String url;
    private String baseUrl;
    private String method;
    private String name;
    private String config;
    private String describe;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer operator;
    private Integer isDelete;
    private Integer version;

}

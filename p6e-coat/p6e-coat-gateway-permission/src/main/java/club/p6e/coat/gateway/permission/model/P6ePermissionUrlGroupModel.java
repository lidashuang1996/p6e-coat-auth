package club.p6e.coat.gateway.permission.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lidashuang
 * @version 1.0
 */
public class P6ePermissionUrlGroupModel implements Serializable {

    private Integer id;
    private String mark;
    private String name;
    private String describe;
    private Integer weight;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer operator;
    private Integer isDelete;
    private Integer version;

}

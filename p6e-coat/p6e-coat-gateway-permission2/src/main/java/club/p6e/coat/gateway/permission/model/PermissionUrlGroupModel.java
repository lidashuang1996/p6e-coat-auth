package club.p6e.coat.gateway.permission.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限组模型
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@Table(PermissionUrlGroupModel.TABLE)
public class PermissionUrlGroupModel implements Serializable {

    public static final String TABLE = "p6e_permission_url_group";

    public static final String ID = "id";
    public static final String MARK = "mark";
    public static final String WEIGHT = "weight";
    public static final String NAME = "name";
    public static final String DESCRIBE = "describe";
    public static final String CREATE_DATE = "createDate";
    public static final String UPDATE_DATE = "updateDate";
    public static final String OPERATOR = "operator";
    public static final String IS_DELETE = "isDelete";
    public static final String VERSION = "version";

    @Id
    private Integer id;
    private String mark;
    private Integer weight;
    private String name;
    private String describe;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer operator;
    private Integer isDelete;
    private Integer version;

}

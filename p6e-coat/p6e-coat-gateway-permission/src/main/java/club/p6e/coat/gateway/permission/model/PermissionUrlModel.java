package club.p6e.coat.gateway.permission.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限 URL 模型
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@Table(PermissionUrlModel.TABLE)
public class PermissionUrlModel implements Serializable {

    public static final String TABLE = "p6e_permission_url";

    public static final String ID = "id";
    public static final String URL = "url";
    public static final String BASE_URL = "baseUrl";
    public static final String METHOD = "method";
    public static final String NAME = "name";
    public static final String CONFIG = "config";
    public static final String DESCRIBE = "describe";
    public static final String CREATE_DATE = "createDate";
    public static final String UPDATE_DATE = "updateDate";
    public static final String OPERATOR = "operator";
    public static final String IS_DELETE = "isDelete";
    public static final String VERSION = "version";

    @Id
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

package club.p6e.auth.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户模型
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@Table(UserModel.TABLE)
public class UserModel implements Serializable {

    public static final String TABLE = "p6e_user";

    public static final String ID = "id";
    public static final String STATUS = "status";
    public static final String ENABLED = "enabled";
    public static final String ACCOUNT = "account";
    public static final String PHONE = "phone";
    public static final String MAILBOX = "mailbox";
    public static final String NAME = "name";
    public static final String NICKNAME = "nickname";
    public static final String AVATAR = "avatar";
    public static final String DESCRIBE = "describe";
    public static final String CREATE_DATE = "createDate";
    public static final String UPDATE_DATE = "updateDate";
    public static final String OPERATOR = "operator";
    public static final String VERSION = "version";
    public static final String IS_DELETE = "isDelete";

    @Id
    private Integer id;
    private Integer status;
    private Integer enabled;
    private String account;
    private String phone;
    private String mailbox;
    private String name;
    private String nickname;
    private String avatar;
    private String describe;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String operator;
    private Integer version;
    private Integer isDelete;

}

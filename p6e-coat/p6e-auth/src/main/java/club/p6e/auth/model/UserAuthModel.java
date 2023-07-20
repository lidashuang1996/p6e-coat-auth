package club.p6e.auth.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户认证模型
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@Table(UserAuthModel.TABLE)
public class UserAuthModel implements Serializable {

    public static final String TABLE = "p6e_user_auth";

    public static final String ID = "id";
    public static final String ACCOUNT = "account";
    public static final String PHONE = "phone";
    public static final String MAILBOX = "mailbox";
    public static final String PASSWORD = "password";
    public static final String QQ = "qq";
    public static final String CREATE_DATE = "createDate";
    public static final String UPDATE_DATE = "updateDate";
    public static final String OPERATOR = "operator";
    public static final String VERSION = "version";
    public static final String IS_DELETE = "isDelete";

    @Id
    private Integer id;
    private String account;
    private String phone;
    private String mailbox;
    private String password;
    private String qq;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String operator;
    private Integer version;
    private Integer isDelete;

}

package club.p6e.cloud.test.infrastructure.model;

import com.darvi.hksi.badminton.lib.Searchable;
import com.darvi.hksi.badminton.lib.Sortable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "p6e_user")
@Where(clause = "is_delete = 0")
public class UserModel implements Serializable {

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
    @Sortable
    @Searchable
    @Column(name = "[id]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty
    @Searchable
    @Column(name = "[status]")
    private Integer status;
    @NotNull
    @Searchable
    @Column(name = "[enabled]")
    private Integer enabled;
    @Size(max = 50)
    @NotEmpty
    @Searchable
    @Column(name = "[account]")
    private String account;
    @Size(max = 20)
    @NotEmpty
    @Searchable
    @Column(name = "[phone]")
    private String phone;
    @Size(max = 100)
    @NotEmpty
    @Email
    @Searchable
    @Column(name = "[mailbox]")
    private String mailbox;
    @Size(max = 50)
    @NotEmpty
    @Searchable
    @Column(name = "[name]")
    private String name;
    @Size(max = 50)
    @NotEmpty
    @Searchable
    @Column(name = "[nickname]")
    private String nickname;
    @Size(max = 200)
    @NotEmpty
    @Column(name = "[avatar]")
    private String avatar;
    @Size(max = 200)
    @NotNull
    @Searchable
    @Column(name = "[describe]")
    private String describe;
    @NotEmpty
    @Sortable
    @Searchable
    @Column(name = "[create_date]")
    private LocalDateTime createDate;
    @NotEmpty
    @Sortable
    @Searchable
    @Column(name = "[update_date]")
    private LocalDateTime updateDate;
    @Size(max = 50)
    @NotEmpty
    @Searchable
    @Column(name = "[operator]")
    private String operator;
    @NotEmpty
    @Column(name = "[version]")
    private Integer version;
    @NotEmpty
    @Column(name = "[is_delete]")
    private Integer isDelete;

}

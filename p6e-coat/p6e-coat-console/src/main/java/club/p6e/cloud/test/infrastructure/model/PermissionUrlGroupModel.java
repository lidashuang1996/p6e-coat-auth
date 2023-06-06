package club.p6e.cloud.test.infrastructure.model;

import com.darvi.hksi.badminton.lib.Searchable;
import com.darvi.hksi.badminton.lib.Sortable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Entity
@Table(name = "p6e_permission_group")
public class PermissionUrlGroupModel implements Serializable {

    public static final String ID = "id";
    public static final String MARK = "mark";
    public static final String WEIGHT = "weight";
    public static final String NAME = "name";
    public static final String DESCRIBE = "describe";
    public static final String CREATE_DATE = "createDate";
    public static final String UPDATE_DATE = "updateDate";
    public static final String OPERATOR = "operator";
    public static final String VERSION = "version";
    public static final String IS_DELETE = "isDelete";

    @Id
    @Column(name = "[id]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Searchable
    @Column(name = "[mark]")
    private String mark;
    @Searchable
    @Column(name = "[weight]")
    private String weight;
    @Searchable
    @Column(name = "[name]")
    private String name;
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
    @Column(name = "[operator]")
    private String operator;
    @NotEmpty
    @Column(name = "[version]")
    private Integer version;
    @NotEmpty
    @Column(name = "[is_delete]")
    private Integer isDelete;

}

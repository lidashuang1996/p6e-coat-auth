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
@Table(name = "p6e_permission_url")
public class PermissionUrlModel implements Serializable {

    public static final String ID = "id";
    public static final String URL = "url";
    public static final String BASE_URL = "baseUrl";
    public static final String METHOD = "method";
    public static final String CONFIG = "config";
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
    @NotEmpty
    @Searchable
    @Column(name = "[url]")
    private String url;
    @NotNull
    @Searchable
    @Column(name = "[base_url]")
    private String baseUrl;
    @Searchable
    @Column(name = "[method]")
    private String method;
    @Searchable
    @Column(name = "[config]")
    private String config;
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

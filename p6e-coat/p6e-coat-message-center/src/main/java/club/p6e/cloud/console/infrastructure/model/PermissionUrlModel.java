package club.p6e.cloud.console.infrastructure.model;

import club.p6e.cloud.console.infrastructure.model.listener.BaseModelListener;
import com.darvi.hksi.badminton.lib.Searchable;
import com.darvi.hksi.badminton.lib.Sortable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "p6e_permission_url")
@Where(clause = "is_delete = 0")
@EntityListeners(value = BaseModelListener.class)
public class PermissionUrlModel implements Serializable {

    public static final String ID = "id";
    public static final String URL = "url";
    public static final String BASE_URL = "baseUrl";
    public static final String METHOD = "method";
    public static final String CONFIG = "config";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
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
    @Column(name = "[description]")
    private String description;
    @Sortable
    @Searchable
    @Column(name = "[create_date]")
    private LocalDateTime createDate;
    @Sortable
    @Searchable
    @Column(name = "[update_date]")
    private LocalDateTime updateDate;
    @Column(name = "[operator]")
    private String operator;
    @Column(name = "[version]")
    private Integer version;
    @Column(name = "[is_delete]")
    private Integer isDelete;

    @Transient
    private String relationConfig;
    @Transient
    private String relationAttribute;

}

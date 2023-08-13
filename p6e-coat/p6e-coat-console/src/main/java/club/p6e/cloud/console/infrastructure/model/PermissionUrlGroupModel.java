package club.p6e.cloud.console.infrastructure.model;

import club.p6e.cloud.console.infrastructure.model.listener.BaseModelListener;
import com.darvi.hksi.badminton.lib.Searchable;
import com.darvi.hksi.badminton.lib.Sortable;
import jakarta.persistence.*;
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
@Table(name = "p6e_permission_url_group")
@Where(clause = "is_delete = 0")
@EntityListeners(value = BaseModelListener.class)
public class PermissionUrlGroupModel implements Serializable {

    public static final String ID = "id";
    public static final String MARK = "mark";
    public static final String WEIGHT = "weight";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CREATE_DATE = "createDate";
    public static final String UPDATE_DATE = "updateDate";
    public static final String OPERATOR = "operator";
    public static final String VERSION = "version";
    public static final String IS_DELETE = "isDelete";

    @Id
    @Column(name = "[id]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Searchable
    @Column(name = "[mark]")
    private String mark;
    @Searchable
    @Column(name = "[weight]")
    private Integer weight;
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

}

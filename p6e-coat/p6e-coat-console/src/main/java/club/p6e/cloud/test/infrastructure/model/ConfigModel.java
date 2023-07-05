package club.p6e.cloud.test.infrastructure.model;

import club.p6e.cloud.test.infrastructure.model.listener.BaseModelListener;
import com.darvi.hksi.badminton.lib.Searchable;
import com.darvi.hksi.badminton.lib.Sortable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "p6e_config")
@EntityListeners(value = BaseModelListener.class)
public class ConfigModel implements Serializable {

    public static final String ID = "id";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String CREATE_DATE = "createDate";
    public static final String UPDATE_DATE = "updateDate";
    public static final String OPERATOR = "operator";
    public static final String VERSION = "version";

    @Id
    @Sortable
    @Searchable
    @Column(name = "[id]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Searchable
    @Column(name = "[key]")
    private String key;
    @Searchable
    @Column(name = "[value]")
    private String value;
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

}

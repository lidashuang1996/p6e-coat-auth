package club.p6e.cloud.console.infrastructure.model;

import club.p6e.cloud.console.infrastructure.model.listener.BaseModelListener;
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
@Accessors(chain = true)
@Entity
@Table(name = "p6e_dictionary")
@EntityListeners(value = BaseModelListener.class)
public class DictionaryModel implements Serializable {

    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String LANGUAGE = "language";
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
    @Column(name = "[type]")
    private String type;
    @Searchable
    @Column(name = "[key]")
    private String key;
    @Searchable
    @Column(name = "[value]")
    private String value;
    @Searchable
    @Column(name = "[language]")
    private String language;
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

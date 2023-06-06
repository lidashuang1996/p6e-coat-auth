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
@Table(name = "p6e_dictionary")
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
    @NotEmpty
    @Searchable
    @Column(name = "[type]")
    private String type;
    @NotNull
    @Searchable
    @Column(name = "[key]")
    private String key;
    @Size(max = 50)
    @NotEmpty
    @Searchable
    @Column(name = "[value]")
    private String value;
    @Searchable
    @Column(name = "[language]")
    private String language;
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

}

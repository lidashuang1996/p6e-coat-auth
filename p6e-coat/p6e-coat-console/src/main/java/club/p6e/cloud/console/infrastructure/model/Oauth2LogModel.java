package club.p6e.cloud.console.infrastructure.model;

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
@Table(name = "p6e_oauth2_log")
public class Oauth2LogModel implements Serializable {

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
    @Column(name = "[cid]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cid;
    @Column(name = "[type]")
    private String uid;
    @Column(name = "[key]")
    private String date;
    @Column(name = "[value]")
    private String action;
    @Column(name = "[content]")
    private String content;
    @Column(name = "[ip]")
    private LocalDateTime ip;

}

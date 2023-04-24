package club.p6e.coat.file.manage.infrastructure.model;

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
@Entity(name = "p6e_fm_file")
@Where(clause = "is_delete = 0")
public class FileModel implements Serializable {

    @Id
    @Column(name = "[id]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "[pid]")
    private Integer pid;
    @Column(name = "[lock]")
    private Integer lock;
    @Column(name = "[name]")
    private String name;
    @Column(name = "[parent]")
    private Integer parent;
    @Column(name = "[owner]")
    private String owner;
    @Column(name = "[edition]")
    private Integer edition;
    @Column(name = "[mediaSize]")
    private Long mediaSize;
    @Column(name = "[mediaType]")
    private String mediaType;
    @Column(name = "[attachment]")
    private Integer attachment;
    @Column(name = "[thumbnail]")
    private String thumbnail;
    @Column(name = "[preview]")
    private String preview;
    @Column(name = "[source]")
    private String source;
    @Column(name = "[create_date]")
    private LocalDateTime createDate;
    @Column(name = "[update_date]")
    private LocalDateTime updateDate;
    @Column(name = "[operator]")
    private String operator;
    @Column(name = "[version]")
    private Integer version;
    @Column(name = "[is_delete]")
    private Integer isDelete;

}

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
@Entity(name = "p6e_fm_folder")
@Where(clause = "is_delete = 0")
public class FolderModel implements Serializable {

    @Id
    @Column(name = "[id]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "[pid]")
    private Integer pid;
    @Column(name = "[name]")
    private String name;
    @Column(name = "[parent]")
    private Integer parent;
    @Column(name = "[owner]")
    private String owner;
    @Column(name = "[size]")
    private Long size;
    @Column(name = "[icon]")
    private String icon;
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

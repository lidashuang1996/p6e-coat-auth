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
@Table(name = "p6e_file_upload")
public class FileUploadModel implements Serializable {

    public static final String ID = "id";
    public static final String NAME = "name";
    @Id
    @Column(name = "[id]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "[name]")
    private String name;
    @Column(name = "[size]")
    private String size;
    @Column(name = "[source]")
    private String source;
    @Column(name = "[storage_location]")
    private String storageLocation;
    @Column(name = "[owner]")
    private String owner;
    @Column(name = "[rubbish]")
    private Integer rubbish;
    @Column(name = "[lock]")
    private Integer lock;
    @Column(name = "[create_date]")
    private LocalDateTime createDate;
    @Column(name = "[update_date]")
    private LocalDateTime updateDate;
    @Column(name = "[operator]")
    private String operator;
    @Column(name = "[version]")
    private Integer version;
}

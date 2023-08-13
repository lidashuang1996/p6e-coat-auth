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
@Table(name = "p6e_file_upload_chunk")
public class FileUploadChunkModel implements Serializable {

    public static final String ID = "id";
    public static final String FID = "fid";
    @Id
    @Column(name = "[id]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "[fid]")
    private Integer fid;
    @Column(name = "[name]")
    private String name;
    @Column(name = "[size]")
    private Integer size;
    @Column(name = "[date]")
    private LocalDateTime date;
    @Column(name = "[operator]")
    private String operator;
}

package club.p6e.coat.file.manage.infrastructure.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@IdClass(PermissionKeyModel.class)
@Accessors(chain = true)
@Entity(name = "p6e_fm_permission")
public class PermissionModel {

    @Id
    @Column(name = "[fid]")
    private Integer fid;
    @Id
    @Column(name = "[type]")
    private String type;
    @Id
    @Column(name = "[content]")
    private Integer content;

}

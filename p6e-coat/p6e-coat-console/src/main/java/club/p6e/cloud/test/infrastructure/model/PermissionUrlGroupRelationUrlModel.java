package club.p6e.cloud.test.infrastructure.model;

import com.darvi.hksi.badminton.lib.Searchable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@IdClass(value = PermissionUrlGroupRelationUrlModel.Key.class)
@Entity
@Table(name = "p6e_permission_url_group_relation_url")
public class PermissionUrlGroupRelationUrlModel implements Serializable {

    public static final String GID = "gid";
    public static final String UID = "uid";

    @Data
    @Accessors(chain = true)
    public static class Key implements Serializable {
        private Integer gid;
        private Integer uid;
    }


    @Id
    @Column(name = "[gid]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gid;
    @Id
    @Column(name = "[uid]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;
    @NotNull
    @Searchable
    @Column(name = "[config]")
    private String config;
    @NotNull
    @Searchable
    @Column(name = "[attribute]")
    private String attribute;

}

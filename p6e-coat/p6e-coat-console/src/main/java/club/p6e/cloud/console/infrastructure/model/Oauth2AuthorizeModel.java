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
@IdClass(Oauth2AuthorizeKeyModel.class)
@Table(name = "p6e_oauth2_authorize")
public class Oauth2AuthorizeModel implements Serializable {
    @Id
    @Column(name = "[cid]")
    private Integer cid;
    @Id
    @Column(name = "[uid]")
    private Integer uid;
    @Column(name = "[date]")
    private LocalDateTime date;
    @Column(name = "[action]")
    private String action;
    @Column(name = "[content]")
    private String content;
    @Column(name = "[version]")
    private Integer version;
    @Column(name = "[create_date]")
    private LocalDateTime createDate;
    @Column(name = "[update_date]")
    private LocalDateTime updateDate;
    @Column(name = "[operator]")
    private String operator;
}

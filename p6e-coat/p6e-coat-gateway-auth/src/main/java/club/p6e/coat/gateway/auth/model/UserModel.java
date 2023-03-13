package club.p6e.coat.gateway.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hjf
 */
@Data
@Entity(name = "sc_user")
@Where(clause = "is_delete=0")
public class UserModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer state;
    private String account;
    private String name;
    private String nickname;
    private String avatar;
    private String describe;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String operator;
    private Integer version;
    private Integer isDelete;
}

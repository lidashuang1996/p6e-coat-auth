package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.model.UserAuthModel;
import club.p6e.coat.gateway.auth.model.UserModel;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
public class AuthUserDetails implements AuthUser {

    private Integer id;
    private Integer status;
    private Integer enabled;
    private String account;
    private String phone;
    private String mailbox;
    private String name;
    private String nickname;
    private String avatar;
    private String describe;
    private
    String password;

    public AuthUserDetails(String content) {

    }

    public AuthUserDetails(UserModel u, UserAuthModel a) {
        this.id = u.getId();
        this.status = u.getStatus();
        this.enabled = u.getEnabled();
        this.account = u.getAccount();
        this.phone = u.getPhone();
        this.mailbox = u.getMailbox();
        this.name = u.getName();
        this.nickname = u.getNickname();
        this.avatar = u.getAvatar();
        this.describe = u.getDescribe();
        this.password = a.getPassword();
    }

    @Override
    public String id() {
        return String.valueOf(id);
    }

    @Override
    public Map<String, Object> toMap() {
        final Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return map;
    }
}

package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.model.UserAuthModel;
import club.p6e.coat.gateway.auth.model.UserModel;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import io.r2dbc.postgresql.codec.Json;
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

    public AuthUserDetails() {
    }

    public static AuthUserDetails create(String content) {
        final AuthUserDetails model = JsonUtil.fromJson(content, AuthUserDetails.class);
        if (model == null) {
            throw new RuntimeException("JSON TO OBJECT ERROR");
        } else {
            return new AuthUserDetails(model);
        }
    }

    public AuthUserDetails(AuthUserDetails u) {
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
        this.password = u.getPassword();
    }

    public AuthUserDetails(UserModel u) {
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

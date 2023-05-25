package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.model.UserAuthModel;
import club.p6e.coat.gateway.auth.model.UserModel;
import club.p6e.coat.gateway.auth.utils.SpringUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthUserDetails implements UserDetails, Serializable {

    private final Integer id;
    private final Integer status;
    private final Integer enabled;
    private final String account;
    private final String phone;
    private final String mailbox;
    private final String name;
    private final String nickname;
    private final String avatar;
    private final String describe;

    /**
     *
     */
    private final String mode;

    /**
     * 用户当前使用的登录账号
     * 可能是 账号/邮箱/手机号码
     */
    private final String username;

    /**
     * 用户设置的账号密码
     */
    private final String password;

    public AuthUserDetails (UserDetails details) {
        if (details instanceof AuthUserDetails) {
            AuthUserDetails d = (AuthUserDetails) details;
            this.username = d.getUsername();
            this.password = d.getPassword();
            this.id = d.getId();
            this.status = d.getStatus();
            this.enabled = d.getEnabled();
            this.account = d.getAccount();
            this.phone = d.getPhone();
            this.mailbox = d.getMailbox();
            this.name = d.getName();
            this.nickname = d.getNickname();
            this.avatar = d.getAvatar();
            this.describe = d.getDescribe();
            this.mode = d.mode;
        } else {
            this.mode = "DEFAULT";
            this.username = details.getUsername();
            this.password = details.getPassword();
            this.id = null;
            this.status = null;
            this.enabled = null;
            this.account = null;
            this.phone = null;
            this.mailbox = null;
            this.name = null;
            this.nickname = null;
            this.avatar = null;
            this.describe = null;
        }
    }

    /**
     * @param username
     * @param um
     * @param uam
     */
    public AuthUserDetails(String username, UserModel um, UserAuthModel uam) {
        final Properties properties = SpringUtil.getBean(Properties.class);
        this.mode = properties.getMode().name();
        this.username = username;
        this.password = uam.getPassword();
        this.id = um.getId();
        this.status = um.getStatus();
        this.enabled = um.getEnabled();
        this.account = um.getAccount();
        this.phone = um.getPhone();
        this.mailbox = um.getMailbox();
        this.name = um.getName();
        this.nickname = um.getNickname();
        this.avatar = um.getAvatar();
        this.describe = um.getDescribe();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(new ArrayList<>());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled != null && enabled == 1;
    }


    public Integer getId() {
        return id;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public String getAccount() {
        return account;
    }

    public String getPhone() {
        return phone;
    }

    public String getMailbox() {
        return mailbox;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDescribe() {
        return describe;
    }

    public String getMode() {
        return mode;
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("status", status);
        result.put("enabled", enabled);
        result.put("account", account);
        result.put("phone", phone);
        result.put("mailbox", mailbox);
        result.put("name", name);
        result.put("nickname", nickname);
        result.put("avatar", avatar);
        result.put("describe", describe);
        return Collections.unmodifiableMap(result);
    }

}

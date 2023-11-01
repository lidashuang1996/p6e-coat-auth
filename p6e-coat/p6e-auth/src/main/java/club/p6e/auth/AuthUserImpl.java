package club.p6e.auth;

import club.p6e.auth.model.UserAuthModel;
import club.p6e.auth.model.UserModel;
import club.p6e.auth.utils.JsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * 认证用户
 *
 * @author lidashuang
 * @version 1.0
 */
public class AuthUserImpl implements AuthUser<AuthUserImpl.Model> {

    @Override
    public Model create(String content) {
        final AuthUserImpl.Model model = JsonUtil.fromJson(content, AuthUserImpl.Model.class);
        if (model == null) {
            throw new RuntimeException("[ " + this.getClass() + " ] create ==> deserialization failure !!");
        } else {
            return model;
        }
    }

    @Override
    public Model create(UserModel um, UserAuthModel uam) {
        final Model model = new Model()
                .setId(um.getId())
                .setStatus(um.getStatus())
                .setEnabled(um.getEnabled())
                .setAccount(um.getAccount())
                .setPhone(um.getPhone())
                .setMailbox(um.getMailbox())
                .setName(um.getName())
                .setNickname(um.getNickname())
                .setAvatar(um.getAvatar())
                .setDescribe(um.getDescription());
        if (uam != null) {
            model.setPassword(uam.getPassword());
        }
        return model;
    }

    /**
     * 实现的用户认证模型
     */
    @Data
    @Accessors(chain = true)
    public static class Model implements AuthUser.Model, Serializable {
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
        private String password;

        @Override
        public String id() {
            return String.valueOf(id);
        }

        @Override
        public String password() {
            return password;
        }

        @Override
        public String serialize() {
            return JsonUtil.toJson(new Model()
                    .setId(this.getId())
                    .setStatus(this.getStatus())
                    .setEnabled(this.getEnabled())
                    .setAccount(this.getAccount())
                    .setPhone(this.getPhone())
                    .setMailbox(this.getMailbox())
                    .setName(this.getName())
                    .setNickname(this.getNickname())
                    .setAvatar(this.getAvatar())
                    .setDescribe(this.getDescribe())
            );
        }

        @Override
        public Map<String, Object> toMap() {
            return null;
        }

    }
}

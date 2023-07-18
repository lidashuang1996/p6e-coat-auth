package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.model.UserAuthModel;
import club.p6e.coat.gateway.auth.model.UserModel;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */

public class AuthUserImpl implements AuthUser<AuthUserImpl.Model> {

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
                .setDescribe(um.getDescribe());
        if (uam != null) {
            model.setPassword(uam.getPassword());
        }
        return model;
    }

    @Override
    public Model create(String content) {
        final AuthUserImpl.Model model = JsonUtil.fromJson(content, AuthUserImpl.Model.class);
        if (model == null) {
            throw new RuntimeException("JSON TO OBJECT ERROR");
        } else {
            return model;
        }
    }

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
            return JsonUtil.toJson(this);
        }
    }
}

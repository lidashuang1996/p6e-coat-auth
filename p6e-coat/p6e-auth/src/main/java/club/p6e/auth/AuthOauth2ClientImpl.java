package club.p6e.auth;

import club.p6e.auth.utils.JsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthOauth2ClientImpl implements AuthOauth2Client<AuthOauth2ClientImpl.Model> {

    @Override
    public Model create(String content) {
        final AuthOauth2ClientImpl.Model model = JsonUtil.fromJson(content, AuthOauth2ClientImpl.Model.class);
        if (model == null) {
            throw new RuntimeException("[ " + this.getClass() + " ] create ==> deserialization failure !!");
        } else {
            return model;
        }
    }

    @Data
    @Accessors(chain = true)
    public static class Model implements AuthOauth2Client.Model, Serializable {
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

    }
}

package club.p6e.coat.auth;

import club.p6e.coat.auth.utils.JsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthOAuth2ClientImpl implements AuthOAuth2Client<AuthOAuth2ClientImpl.Model> {

    @Override
    public Model create(String content) {
        final AuthOAuth2ClientImpl.Model model = JsonUtil.fromJson(content, AuthOAuth2ClientImpl.Model.class);
        if (model == null) {
            throw new RuntimeException("[ " + this.getClass() + " ] create ==> deserialization failure !!");
        } else {
            return model;
        }
    }

    @Data
    @Accessors(chain = true)
    public static class Model implements AuthOAuth2Client.Model, Serializable {
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

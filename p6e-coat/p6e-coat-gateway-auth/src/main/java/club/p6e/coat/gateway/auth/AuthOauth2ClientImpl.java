package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.utils.JsonUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */

public class AuthOauth2ClientImpl implements AuthOauth2Client<AuthOauth2Client.Model> {

    @Override
    public Model create(String content) {
        final AuthOauth2ClientImpl.Model model = JsonUtil.fromJson(content, AuthOauth2ClientImpl.Model.class);
        if (model == null) {
            throw new RuntimeException("JSON TO OBJECT ERROR");
        } else {
            return model;
        }
    }

    @Data
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
            return JsonUtil.toJson(this);
        }
    }
}

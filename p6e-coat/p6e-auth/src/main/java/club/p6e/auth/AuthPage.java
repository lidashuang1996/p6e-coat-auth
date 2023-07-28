package club.p6e.auth;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.MediaType;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class AuthPage {

    @Data
    @Accessors(chain = true)
    public static class Model implements Serializable {
        private MediaType type;
        private String content;

        public Model(String content) {
            this.content = content;
            this.type = MediaType.TEXT_HTML;
        }

        public Model(MediaType type, String content) {
            this.type = type;
            this.content = content;
        }
    }

    private static final Model ME = new Model("I am on the me page.");
    private static final Model LOGIN = new Model("I am on the login page.");

    private static final Model REGISTER = new Model("I am on the register page.");

    public static Model me() {
        return ME;
    }

    public static void me(MediaType type) {
        ME.setType(type);
    }

    public static void me(String content) {
        ME.setContent(content);
    }

    public static void me(MediaType type, String content) {
        ME.setType(type);
        ME.setContent(content);
    }

    public static Model login() {
        return LOGIN;
    }

    public static void login(MediaType type) {
        LOGIN.setType(type);
    }

    public static void login(String content) {
        LOGIN.setContent(content);
    }

    public static void login(MediaType type, String content) {
        LOGIN.setType(type);
        LOGIN.setContent(content);
    }


    public static Model register() {
        return REGISTER;
    }

    public static void register(MediaType type) {
        REGISTER.setType(type);
    }

    public static void register(String content) {
        REGISTER.setContent(content);
    }

    public static void register(MediaType type, String content) {
        REGISTER.setType(type);
        REGISTER.setContent(content);
    }

}

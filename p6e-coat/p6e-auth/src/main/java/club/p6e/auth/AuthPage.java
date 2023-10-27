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

    /**
     * ME PAGE
     */
    private static final Model ME = new Model("I am on the me page.");

    /**
     * LOGIN PAGE
     */
    private static final Model LOGIN = new Model("I am on the login page.");

    /**
     * REGISTER PAGE
     */
    private static final Model OAUTH2_CONFIRM = new Model("I am on the register page.");

    /**
     * REGISTER PAGE
     */
    private static final Model REGISTER = new Model("I am on the register page.");

    /**
     * FORGOT PASSWORD PAGE
     */
    private static final Model FORGOT_PASSWORD = new Model("I am on the forgot password page.");

    /**
     * @return
     */
    public static Model me() {
        return ME;
    }

    /**
     * @param type
     */
    public static void setMe(MediaType type) {
        ME.setType(type);
    }

    /**
     * @param content 内容
     */
    public static void setMe(String content) {
        ME.setContent(content);
    }

    /**
     * @param type
     * @param content 内容
     */
    public static void setMe(MediaType type, String content) {
        ME.setType(type);
        ME.setContent(content);
    }

    /**
     * @return 登录页面
     */
    public static Model login() {
        return LOGIN;
    }

    /**
     * 设置登录页面
     *
     * @param type 类型
     */
    public static void setLogin(MediaType type) {
        LOGIN.setType(type);
    }

    /**
     * 设置登录页面
     *
     * @param content 内容
     */
    public static void setLogin(String content) {
        LOGIN.setContent(content);
    }

    /**
     * 设置登录页面
     *
     * @param type    类型
     * @param content 内容
     */
    public static void setLogin(MediaType type, String content) {
        LOGIN.setType(type);
        LOGIN.setContent(content);
    }

    /**
     * @return 注册页面
     */
    public static Model register() {
        return REGISTER;
    }

    /**
     * 设置注册页面
     *
     * @param type 类型
     */
    public static void setRegister(MediaType type) {
        REGISTER.setType(type);
    }

    /**
     * 设置注册页面
     *
     * @param content 内容
     */
    public static void setRegister(String content) {
        REGISTER.setContent(content);
    }

    /**
     * 设置注册页面
     *
     * @param type    类型
     * @param content 内容
     */
    public static void setRegister(MediaType type, String content) {
        REGISTER.setType(type);
        REGISTER.setContent(content);
    }

    /**
     * @return 忘记密码页面
     */
    public static Model forgotPassword() {
        return FORGOT_PASSWORD;
    }

    /**
     * 设置忘记密码页面
     *
     * @param type 类型
     */
    public static void setForgotPassword(MediaType type) {
        FORGOT_PASSWORD.setType(type);
    }

    /**
     * 设置忘记密码页面
     *
     * @param content 内容
     */
    public static void setForgotPassword(String content) {
        FORGOT_PASSWORD.setContent(content);
    }

    /**
     * 设置忘记密码页面
     *
     * @param type    类型
     * @param content 内容
     */
    public static void setForgotPassword(MediaType type, String content) {
        FORGOT_PASSWORD.setType(type);
        FORGOT_PASSWORD.setContent(content);
    }

    public static Model oAuth2Confirm() {
        return OAUTH2_CONFIRM;
    }

    /**
     * 模型类
     */
    @Data
    @Accessors(chain = true)
    public static class Model implements Serializable {

        /**
         * 类型
         */
        private MediaType type;

        /**
         * 内容
         */
        private String content;

        /**
         * 构造方法
         *
         * @param content 内容
         */
        public Model(String content) {
            this.content = content;
            this.type = MediaType.TEXT_HTML;
        }

        /**
         * 构造方法
         *
         * @param type    类型
         * @param content 内容
         */
        public Model(MediaType type, String content) {
            this.type = type;
            this.content = content;
        }
    }
}

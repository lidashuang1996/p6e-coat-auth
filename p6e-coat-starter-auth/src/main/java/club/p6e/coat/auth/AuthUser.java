package club.p6e.coat.auth;

import club.p6e.coat.auth.model.UserAuthModel;
import club.p6e.coat.auth.model.UserModel;

import java.io.Serializable;
import java.util.Map;

/**
 * 认证用户
 *
 * @author lidashuang
 * @version 1.0
 */
public interface AuthUser<M extends AuthUser.Model> {

    /**
     * 认证用户数据的模型
     */
    interface Model extends Serializable {

        /**
         * 获取序号
         *
         * @return 序号
         */
        public String id();

        /**
         * 获取密码
         *
         * @return 密码
         */
        public String password();

        /**
         * 序列化方法
         *
         * @return 序列化后的字符串内容
         */
        public String serialize();

        public Map<String, Object> toMap();

    }

    /**
     * 创建认证用户模型
     *
     * @param content 序列化的字符串内容
     * @return 认证用户模型
     */
    public M create(String content);

    /**
     * 创建认证用户模型
     *
     * @param um  用户模型
     * @param uam 用户认证模型
     * @return 认证用户模型
     */
    public M create(UserModel um, UserAuthModel uam);

}

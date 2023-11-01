package club.p6e.auth;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthOAuth2Client<M extends AuthOAuth2Client.Model> {

    /**
     * 认证客户端数据的模型
     */
    public interface Model extends Serializable {

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

    }

    /**
     * 创建认证客户端模型
     *
     * @param content 序列化的字符串内容
     * @return 认证客户端模型
     */
    public M create(String content);

}

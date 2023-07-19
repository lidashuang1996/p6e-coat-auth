package club.p6e.coat.gateway.auth.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录的上下文对象
 *
 * @author lidashuang
 * @version 1.0
 */
public class RegisterContext implements Serializable {

    /**
     * 登录的请求对象
     */
    @Data
    @Accessors(chain = true)
    public static class Request implements Serializable {
        private String account;
        private String password;
    }

    @Data
    @Accessors(chain = true)
    public static class Dto implements Serializable {
        private Integer id;
        private String account;
        private Integer status;
        private String name;
        private String nickname;
        private String avatar;
        private String describe;
    }

}

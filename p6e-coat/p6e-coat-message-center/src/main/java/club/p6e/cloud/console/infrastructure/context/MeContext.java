package club.p6e.cloud.console.infrastructure.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
public class MeContext implements Serializable {

    public static class Info implements Serializable {

        @Data
        @Accessors(chain = true)
        public static class Vo implements Serializable {
            private Integer id;
            private String account;
            private Integer state;
            private Integer role;
            private String roleName;
            private String name;
            private String nickname;
            private String avatar;
            private String describe;
        }

        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private Integer id;
            private String account;
            private Integer state;
            private Integer role;
            private String roleName;
            private String name;
            private String nickname;
            private String avatar;
            private String describe;
        }

    }


    public static class ChangePassword implements Serializable {

        @Data
        @Accessors(chain = true)
        public static class Request implements Serializable {
            private Integer id;

            private String oldPassword;
            private String newPassword;
        }

        @Data
        @Accessors(chain = true)
        public static class Vo implements Serializable {
            private String content;
        }

        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private String content;
        }

    }
}

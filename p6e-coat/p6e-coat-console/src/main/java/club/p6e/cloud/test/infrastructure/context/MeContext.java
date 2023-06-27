package club.p6e.cloud.test.infrastructure.context;

import com.darvi.hksi.badminton.lib.Searchable;
import com.darvi.hksi.badminton.lib.SearchableContext;
import com.darvi.hksi.badminton.lib.Sortable;
import com.darvi.hksi.badminton.lib.SortableContext;
import com.darvi.hksi.badminton.lib.context.BaseContext;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

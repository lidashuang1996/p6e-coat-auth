package club.p6e.cloud.test.infrastructure.context;

import com.darvi.hksi.badminton.lib.SearchableContext;
import com.darvi.hksi.badminton.lib.SortableContext;
import com.darvi.hksi.badminton.lib.context.BaseContext;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public class FileUploadLogContext implements Serializable {

    @Data
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Request extends BaseContext.PagingParam implements Serializable {
        private String query;
    }

    @Data
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class ListVo extends BaseContext.ListResult implements Serializable {
        private List<Item> list;
    }

    @Data
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class ListDto extends BaseContext.ListResult implements Serializable {
        private List<Item> list;
    }

    @Data
    @Accessors(chain = true)
    public static class Item implements Serializable {
        private Integer id;
        private String name;
        private String size;
        private String source;
        private String storageLocation;
        private String owner;
        private Integer rubbish;
        private Integer lock;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;
        private String operator;
    }


    public static class Details implements Serializable {
        @Data
        @Accessors(chain = true)
        public static class Request implements Serializable {
            private Integer id;
        }

        @Data
        @Accessors(chain = true)
        @EqualsAndHashCode(callSuper = true)
        public static class Vo extends Model implements Serializable {
        }

        @Data
        @Accessors(chain = true)
        @EqualsAndHashCode(callSuper = true)
        public static class Dto extends Model implements Serializable {
        }

        @Data
        @Accessors(chain = true)
        public static class Model implements Serializable {
            private Integer id;
            private String name;
            private String size;
            private String source;
            private String storageLocation;
            private String owner;
            private Integer rubbish;
            private Integer lock;
            private LocalDateTime createDate;
            private LocalDateTime updateDate;
            private String operator;
            private List<ChunkModel> list = new ArrayList<>();
        }

        @Data
        @Accessors(chain = true)
        public static class ChunkModel implements Serializable {
            private Integer id;
            private Integer fid;
            private String name;
            private Integer size;
            private LocalDateTime date;
            private String operator;
        }
    }

}

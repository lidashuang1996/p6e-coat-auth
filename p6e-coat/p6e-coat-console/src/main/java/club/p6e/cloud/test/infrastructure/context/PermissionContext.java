package club.p6e.cloud.test.infrastructure.context;

import com.darvi.hksi.badminton.lib.Searchable;
import com.darvi.hksi.badminton.lib.SearchableContext;
import com.darvi.hksi.badminton.lib.Sortable;
import com.darvi.hksi.badminton.lib.SortableContext;
import com.darvi.hksi.badminton.lib.context.BaseContext;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class PermissionContext implements Serializable {

    public static class Url implements Serializable {
        @Data
        @Accessors(chain = true)
        @EqualsAndHashCode(callSuper = true)
        public static class Request extends BaseContext.PagingParam implements Serializable {
            private String query;
            private SortableContext<SortableContext.Option> sort;
            private SearchableContext<SearchableContext.Option> search;

            private Integer id;
            private String url;
            private String baseUrl;
            private String method;
            private String config;
            private String name;
            private String describe;
        }

        @Data
        @Accessors(chain = true)
        public static class Vo implements Serializable {
            private Integer id;
            private String url;
            private String baseUrl;
            private String method;
            private String config;
            private String name;
            private String describe;
            private LocalDateTime createDate;
            private LocalDateTime updateDate;
            private String operator;
        }

        @Data
        @Accessors(chain = true)
        @EqualsAndHashCode(callSuper = true)
        public static class ListVo extends BaseContext.ListResult implements Serializable {
            private List<Item> list;
        }


        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private Integer id;
            private String url;
            private String baseUrl;
            private String method;
            private String config;
            private String name;
            private String describe;
            private LocalDateTime createDate;
            private LocalDateTime updateDate;
            private String operator;
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
            private String url;
            private String baseUrl;
            private String method;
            private String config;
            private String name;
            private String describe;
            private LocalDateTime createDate;
            private LocalDateTime updateDate;
            private String operator;
        }
    }

    public static class UrlGroup implements Serializable {
        @Data
        @Accessors(chain = true)
        @EqualsAndHashCode(callSuper = true)
        public static class Request extends BaseContext.PagingParam implements Serializable {
            private String query;
            private SortableContext<SortableContext.Option> sort;
            private SearchableContext<SearchableContext.Option> search;

            private Integer id;
            private String mark;
            private String weight;
            private String name;
            private String describe;
        }

        @Data
        @Accessors(chain = true)
        public static class Vo implements Serializable {
            private Integer id;
            private String url;
            private String baseUrl;
            private String method;
            private String config;
            private String name;
            private String describe;
            private LocalDateTime createDate;
            private LocalDateTime updateDate;
            private String operator;
        }

        @Data
        @Accessors(chain = true)
        @EqualsAndHashCode(callSuper = true)
        public static class ListVo extends BaseContext.ListResult implements Serializable {
            private List<Item> list;
        }


        @Data
        @Accessors(chain = true)
        public static class Dto implements Serializable {
            private Integer id;
            private String mark;
            private String weight;
            private String name;
            private String describe;
            private LocalDateTime createDate;
            private LocalDateTime updateDate;
            private String operator;
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
            private String mark;
            private String weight;
            private String name;
            private String describe;
            private LocalDateTime createDate;
            private LocalDateTime updateDate;
            private String operator;
        }

        public static class Details implements Serializable {
            @Data
            @Accessors(chain = true)
            @EqualsAndHashCode(callSuper = true)
            public static class Request extends BaseContext.PagingParam implements Serializable {
                private Integer id;
            }

            @Data
            @Accessors(chain = true)
            @EqualsAndHashCode(callSuper = true)
            public static class Vo extends BaseContext.ListResult implements Serializable {
                private Integer id;
                private String mark;
                private String weight;
                private String name;
                private String describe;
                private LocalDateTime createDate;
                private LocalDateTime updateDate;
                private String operator;
                private Integer version;
                private List<Url> urls;
            }


            @Data
            @Accessors(chain = true)
            @EqualsAndHashCode(callSuper = true)
            public static class Dto extends BaseContext.ListResult implements Serializable {
                private Integer id;
                private String mark;
                private String weight;
                private String name;
                private String describe;
                private LocalDateTime createDate;
                private LocalDateTime updateDate;
                private String operator;
                private Integer version;
                private List<Url> urls;
            }

            @Data
            @Accessors(chain = true)
            public static class Url implements Serializable {
                private Integer id;
                private String url;
                private String baseUrl;
                private String method;
                private String config;
                private String name;
                private String describe;
                private LocalDateTime createDate;
                private LocalDateTime updateDate;
                private String operator;
                private String relationConfig;
                private String relationAttribute;
            }
        }


    }
}

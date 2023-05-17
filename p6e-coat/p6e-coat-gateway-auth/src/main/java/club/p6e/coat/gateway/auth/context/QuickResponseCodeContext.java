package club.p6e.coat.gateway.auth.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 分片上传上下文对象
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
public class QuickResponseCodeContext implements Serializable {

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Request extends VoucherContext.Request implements Serializable {
        private String content;
    }

    @Data
    @Accessors(chain = true)
    public static class Obtain implements Serializable {
        @Data
        @EqualsAndHashCode(callSuper = true)
        public static class Request extends VoucherContext.Request {
        }

        @Data
        public static class Dto {
            private String content;
        }

    }

}

package club.p6e.coat.gateway.auth.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.codec.multipart.FilePart;

import java.io.Serializable;

/**
 * 分片上传上下文对象
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
public class VerificationCodeLoginContext implements Serializable {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Request extends VoucherContext.Request {
        private String code;
    }

    @Data
    @Accessors(chain = true)
    public static class Obtain implements Serializable {
        @Data
        @EqualsAndHashCode(callSuper = true)
        public static class Request extends VoucherContext.Request {
            private String account;

        }

        @Data
        public static class Dto {
            private String content;
        }

    }

}

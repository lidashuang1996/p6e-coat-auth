package club.p6e.coat.gateway.auth.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
        private String key;
        private String value;
    }

}

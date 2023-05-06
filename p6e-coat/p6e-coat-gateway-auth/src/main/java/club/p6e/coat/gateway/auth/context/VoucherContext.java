package club.p6e.coat.gateway.auth.context;

import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

import java.io.Serializable;

/**
 * 分片上传上下文对象
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
public class VoucherContext implements Serializable {

    @Data
    public static class Request implements Serializable {
        private String voucher;
    }

}

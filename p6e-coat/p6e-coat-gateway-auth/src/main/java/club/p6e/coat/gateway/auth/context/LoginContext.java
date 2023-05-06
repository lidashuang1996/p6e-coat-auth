package club.p6e.coat.gateway.auth.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.codec.multipart.FilePart;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 分片上传上下文对象
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
public class LoginContext implements Serializable {

    /**
     * 上传编号
     */
    private Integer id;

    /**
     * 分片索引
     */
    private Integer index;

    /**
     * 分片签名
     */
    private String signature;

    /**
     * 文件对象
     */
    private FilePart filePart;

    /**
     * 无参数构造
     */
    public LoginContext() {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Request extends VoucherContext.Request {
        /**
         * 上传编号
         */
        private String account;

        /**
         * 分片索引
         */
        private String password;
    }

}

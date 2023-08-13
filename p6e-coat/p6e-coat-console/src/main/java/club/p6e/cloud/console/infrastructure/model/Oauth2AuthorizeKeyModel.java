package club.p6e.cloud.console.infrastructure.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class Oauth2AuthorizeKeyModel implements Serializable {
    private Integer cid;
    private Integer uid;
}

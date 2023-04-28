package club.p6e.coat.file;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = FileSignatureService.class,
        ignored = FileSignatureServiceImpl.class
)
public class FileSignatureServiceImpl implements FileSignatureService {

    public String execute(byte[] bytes) {
        return "";
    }

}

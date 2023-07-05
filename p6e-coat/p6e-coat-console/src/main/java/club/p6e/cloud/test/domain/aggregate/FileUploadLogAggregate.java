package club.p6e.cloud.test.domain.aggregate;

import club.p6e.cloud.test.Properties;
import club.p6e.cloud.test.domain.ConfigurationDomain;
import club.p6e.cloud.test.infrastructure.model.FileUploadModel;
import club.p6e.cloud.test.infrastructure.repository.FileUploadRepository;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public class FileUploadLogAggregate extends ConfigurationDomain {

    public static void storage() {
        final Properties properties = SpringUtil.getBean(Properties.class);
        final String path = properties.getFileUpload().getPath();
        System.out.println(">>>>>>>> " + path);
    }

}

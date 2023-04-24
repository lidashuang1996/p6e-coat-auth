package club.p6e.coat.file.manage.infrastructure.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author lidashuang
 * @version 1.0
 */

@Document(indexName = "my_index")
public class EsFileRepository {

    @Id
    private String id;
    private String name;
    private String description;
    // getter 和 setter 略
}

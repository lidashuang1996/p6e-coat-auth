package club.p6e.coat.file.manage.infrastructure.es;

import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class EsFileRepository {

    private final ElasticsearchTemplate template;

    public EsFileRepository(ElasticsearchTemplate template) {
        this.template = template;
    }

    public void create() {



    }
}

package club.p6e.coat.file.manage.infrastructure.es;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class EsRepository {

    /**
     * ES 索引
     */
    private static final IndexCoordinates INDEX = IndexCoordinates.of("test");

    private static final Logger LOGGER = LoggerFactory.getLogger(EsRepository.class);

    /**
     * ES Template 对象
     */
    private final ElasticsearchTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template ES Template 对象
     */
    public EsRepository(ElasticsearchTemplate template) {
        this.template = template;
        final IndexOperations index = this.template.indexOps(INDEX);
        if (index.exists()) {
            LOGGER.info("1111");
        } else {
            index.create();
            LOGGER.info("222");
        }
    }

    public void select() {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withIndexName("user_index")
                .withType("user_type")
                .withId(user.getId())
                .withObject(user)
                .build();
        template.doIndex(indexQuery, INDEX);
    }

    public void create() {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withIndexName("user_index")
                .withType("user_type")
                .withId(user.getId())
                .withObject(user)
                .build();
        template.doIndex(indexQuery, INDEX);
    }

    public void update() {
    }

    public void delete() {
        template.delete(Query.multiGetQuery().findAll(), INDEX);
    }
}

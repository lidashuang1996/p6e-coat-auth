package club.p6e.coat.file.manage.infrastructure.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;

/**
 * @author lidashuang
 * @version 1.0
 */
@Configuration
public class EsConfig {

    @Bean
    public ElasticsearchTemplate client() {
        final RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();
        final ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        final ElasticsearchClient client = new ElasticsearchClient(transport);
        return new ElasticsearchTemplate(client);
    }


}

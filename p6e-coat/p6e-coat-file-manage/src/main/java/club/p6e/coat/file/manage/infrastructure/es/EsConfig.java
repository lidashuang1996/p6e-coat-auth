package club.p6e.coat.file.manage.infrastructure.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.erhlc.RestClients;

/**
 * @author lidashuang
 * @version 1.0
 */
@Configuration
public class EsConfig {
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private int port;
    @Value("${elasticsearch.scheme}")
    private String scheme;

    @Bean
    public RestHighLevelClient client() {
        return RestClients.ElasticsearchRestClient
    }


}

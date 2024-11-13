package clients;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import elk.utils.DTO.Vacancy;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * ApiClient для работы с ElasticSearch
 */
public class ElasticSearchApiClient {

    private static final Logger LOGGER = Logger.getLogger(ElasticSearchApiClient.class.getName());
    private static final String ES_HOST_PROP = "host";
    private static final String ES_PORT_PROP = "port";
    private static final String ES_SCHEME_PROP = "http";
    private static final String ES_USERNAME_PROP = "username";
    private static final String ES_PASSWORD_PROP = "password";

    private ElasticsearchClient elasticsearchClient;

    public ElasticSearchApiClient() {
        String host = System.getProperty(ES_HOST_PROP);
        int port = Integer.parseInt(System.getProperty(ES_PORT_PROP));
        String scheme = System.getProperty(ES_SCHEME_PROP);

        LOGGER.info("Конфигурируем api-клиент ElasticSearch на хосте: " + host + ":" + port);

        String username = System.getProperty(ES_USERNAME_PROP);
        String password = System.getProperty(ES_PASSWORD_PROP);

        LOGGER.info("Настраиваем аутентификацию");
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(host, port),
                new UsernamePasswordCredentials(username, password)
        );

        RestClient restClient = RestClient
                .builder(new HttpHost(host, port, scheme))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                .build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        elasticsearchClient = new ElasticsearchClient(transport);
    }

    public void addVacancyToIndex(Vacancy vacancy, String index) {
        LOGGER.info("Загружаем данные в индекс = " + index);
        try {
            elasticsearchClient.index(i -> i
                    .index(index)
                    .id(String.valueOf(vacancy.getId()))
                    .document(vacancy));
            System.out.println();
        } catch (IOException e) {
            LOGGER.warning("Ошибка при загрузке данных в индекс = " + index);
        }
    }

    public List<Hit<Vacancy>> getVacancyByFieldText(String field, String text, String index) {
        LOGGER.info("Ище вакансию по " + field  + " = " + text);
        try {
            return elasticsearchClient.search(s -> s
                            .index(index)
                            .query(q -> q
                                    .match(t -> t
                                            .field(field)
                                            .query(text)
                                    )
                            ),
                    Vacancy.class)
                    .hits()
                    .hits();
        } catch (IOException e) {
            LOGGER.warning("Ошибка при поиске вакансии по " + field  + " = " + text);
            throw new RuntimeException(e);
        }
    }

}

package clients;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.BooleanProperty;
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
import co.elastic.clients.elasticsearch._types.mapping.LongNumberProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.PutMappingRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCursor;
import elk.utils.DTO.Vacancy;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.bson.Document;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private RestClient restClient;

    /**
     * Создание клиента ElasticSearch
     */
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

        restClient = RestClient
                .builder(new HttpHost(host, port, scheme))
                .setRequestConfigCallback(requestConfigBuilder ->
                        requestConfigBuilder
                                .setConnectTimeout(5000)
                                .setSocketTimeout(60000))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                .build();
        elasticsearchClient = new ElasticsearchClient(new RestClientTransport(restClient, new JacksonJsonpMapper()));
    }

    /**
     * Добавляем вакансию в индекс
     *
     * @param vacancy - вакансия
     * @param index - индекс
     */
    public void addVacancyToIndex(Vacancy vacancy, String index) {
        LOGGER.info("Загружаем данные в индекс " + index);
        try {
            elasticsearchClient.index(i -> i
                    .index(index)
                    .id(String.valueOf(vacancy.getId()))
                    .document(vacancy));
        } catch (IOException e) {
            LOGGER.warning("Ошибка при загрузке данных в индекс = " + index);
        }
    }

    /**
     * Создаем индекс
     * @param indexName - индекс
     * @return - успешность создания индекса
     */
    public boolean createIndex(String indexName) {
        LOGGER.info("Создаем индекс " + indexName + " с маппингом");
        CreateIndexRequest request = new CreateIndexRequest.Builder()
                .index(indexName)
                .mappings(m -> m
                        .properties("area", Property.of(p -> p
                                .object(o -> o
                                        .properties("name", Property.of(p2 -> p2.keyword(KeywordProperty.of(tp -> tp))))
                                        .properties("id", Property.of(p2 -> p2.long_(LongNumberProperty.of(kp -> kp))))
                                        .properties("url", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                )
                        ))
                        .properties("key_skills", Property.of(p -> p
                                .nested(n -> n
                                        .properties("name", Property.of(p2 -> p2.keyword(KeywordProperty.of(tp -> tp))))
                                )
                        ))
                        .properties("initial_created_at", Property.of(p -> p
                                .date(d -> d.format("strict_date_optional_time||epoch_millis"))
                        ))
                        .properties("professional_roles", Property.of(p -> p
                                .nested(n -> n
                                        .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                        .properties("id", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                                )
                        ))
                        .properties("working_days", Property.of(p -> p
                                .nested(n -> n
                                        .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                        .properties("id", Property.of(p2 -> p2.keyword(KeywordProperty.of(kp -> kp))))
                                )
                        ))
                        .properties("working_time_intervals", Property.of(p -> p
                                .nested(n -> n
                                        .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                        .properties("id", Property.of(p2 -> p2.keyword(KeywordProperty.of(kp -> kp))))
                                )
                        ))
                        .properties("accept_handicapped", Property.of(p -> p.boolean_(BooleanProperty.of(bp -> bp))))
                        .properties("description", Property.of(p -> p.text(TextProperty.of(tp -> tp))))
                        .properties("employment", Property.of(p -> p
                                .object(o -> o
                                        .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                        .properties("id", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                                )
                        ))
                        .properties("experience", Property.of(p -> p
                                .object(o -> o
                                        .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                        .properties("id", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                                )
                        ))
                        .properties("working_time_modes", Property.of(p -> p
                                .nested(n -> n
                                        .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                        .properties("id", Property.of(p2 -> p2.keyword(KeywordProperty.of(kp -> kp))))
                                )
                        ))
                        .properties("schedule", Property.of(p -> p
                                .object(o -> o
                                        .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                        .properties("id", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                )
                        ))
                        .properties("name", Property.of(p -> p.text(TextProperty.of(tp -> tp))))
                        .properties("employer", Property.of(p -> p
                                .object(o -> o
                                        .properties("accredited_it_employer", Property.of(p2 -> p2.boolean_(BooleanProperty.of(bp -> bp))))
                                        .properties("trusted", Property.of(p2 -> p2.boolean_(BooleanProperty.of(bp -> bp))))
                                        .properties("logo_urls", Property.of(p2 -> p2
                                                .object(o2 -> o2
                                                        .properties("90", Property.of(p3 -> p3.text(TextProperty.of(kp -> kp))))
                                                        .properties("240", Property.of(p3 -> p3.text(TextProperty.of(kp -> kp))))
                                                        .properties("original", Property.of(p3 -> p3.text(TextProperty.of(kp -> kp))))
                                                )
                                        ))
                                        .properties("vacancies_url", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                                        .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                        .properties("id", Property.of(p2 -> p2.long_(LongNumberProperty.of(kp -> kp))))
                                        .properties("alternate_url", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                                        .properties("url", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                                )
                        ))
                        .properties("accept_temporary", Property.of(p -> p.boolean_(BooleanProperty.of(bp -> bp))))
                        .properties("id", Property.of(p -> p.long_(LongNumberProperty.of(kp -> kp))))
                        .properties("published_at", Property.of(p -> p
                                .date(d -> d.format("strict_date_optional_time||epoch_millis"))
                        ))
                )
                .build();

        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = elasticsearchClient.indices().create(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return createIndexResponse.acknowledged();
    }

    /**
     * Обновляем индекс
     * @param indexName - название индекса
     */
    public void updateIndexWithMapping(String indexName) {
        LOGGER.info("Обновляем индекс " + indexName + " в соответствии с полями БД");
        PutMappingRequest request = new PutMappingRequest.Builder()
                .index(indexName)
                .properties("area", Property.of(p -> p
                        .object(o -> o
                                .properties("name", Property.of(p2 -> p2.keyword(KeywordProperty.of(tp -> tp))))
                                .properties("id", Property.of(p2 -> p2.long_(LongNumberProperty.of(kp -> kp))))
                                .properties("url", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                        )
                ))
                .properties("key_skills", Property.of(p -> p
                        .nested(n -> n
                                .properties("name", Property.of(p2 -> p2.keyword(KeywordProperty.of(tp -> tp))))
                        )
                ))
                .properties("initial_created_at", Property.of(p -> p
                        .date(d -> d.format("strict_date_optional_time||epoch_millis"))
                ))
                .properties("professional_roles", Property.of(p -> p
                        .nested(n -> n
                                .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                .properties("id", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                        )
                ))
                .properties("working_days", Property.of(p -> p
                        .nested(n -> n
                                .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                .properties("id", Property.of(p2 -> p2.keyword(KeywordProperty.of(kp -> kp))))
                        )
                ))
                .properties("working_time_intervals", Property.of(p -> p
                        .nested(n -> n
                                .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                .properties("id", Property.of(p2 -> p2.keyword(KeywordProperty.of(kp -> kp))))
                        )
                ))
                .properties("accept_handicapped", Property.of(p -> p.boolean_(BooleanProperty.of(bp -> bp))))
                .properties("description", Property.of(p -> p.text(TextProperty.of(tp -> tp))))
                .properties("employment", Property.of(p -> p
                        .object(o -> o
                                .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                .properties("id", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                        )
                ))
                .properties("experience", Property.of(p -> p
                        .object(o -> o
                                .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                .properties("id", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                        )
                ))
                .properties("working_time_modes", Property.of(p -> p
                        .nested(n -> n
                                .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                .properties("id", Property.of(p2 -> p2.keyword(KeywordProperty.of(kp -> kp))))
                        )
                ))
                .properties("schedule", Property.of(p -> p
                        .object(o -> o
                                .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                .properties("id", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                        )
                ))
                .properties("name", Property.of(p -> p.text(TextProperty.of(tp -> tp))))
                .properties("employer", Property.of(p -> p
                        .object(o -> o
                                .properties("accredited_it_employer", Property.of(p2 -> p2.boolean_(BooleanProperty.of(bp -> bp))))
                                .properties("trusted", Property.of(p2 -> p2.boolean_(BooleanProperty.of(bp -> bp))))
                                .properties("logo_urls", Property.of(p2 -> p2
                                        .object(o2 -> o2
                                                .properties("90", Property.of(p3 -> p3.text(TextProperty.of(kp -> kp))))
                                                .properties("240", Property.of(p3 -> p3.text(TextProperty.of(kp -> kp))))
                                                .properties("original", Property.of(p3 -> p3.text(TextProperty.of(kp -> kp))))
                                        )
                                ))
                                .properties("vacancies_url", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                                .properties("name", Property.of(p2 -> p2.text(TextProperty.of(tp -> tp))))
                                .properties("id", Property.of(p2 -> p2.long_(LongNumberProperty.of(kp -> kp))))
                                .properties("alternate_url", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                                .properties("url", Property.of(p2 -> p2.text(TextProperty.of(kp -> kp))))
                        )
                ))
                .properties("accept_temporary", Property.of(p -> p.boolean_(BooleanProperty.of(bp -> bp))))
                .properties("id", Property.of(p -> p.long_(LongNumberProperty.of(kp -> kp))))
                .properties("published_at", Property.of(p -> p
                        .date(d -> d.format("strict_date_optional_time||epoch_millis"))
                ))
                .build();
        try {
            elasticsearchClient.indices().putMapping(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Проверяем существования индекса
     * @param indexName - название индекса
     * @return - существование индекса
     */
    public boolean isIndexExists(String indexName) {
        boolean exists;
        try {
            exists = elasticsearchClient.indices().exists(e -> e.index(indexName)).value();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return exists;
    }

    /**
     * Удалям индекс
     * @param index - индекс
     * @return успешность удаления индекса
     */
    public boolean deleteIndex(String index) {
        LOGGER.info("Удаляем индекс " + index);
        try {
            DeleteIndexRequest request = new DeleteIndexRequest.Builder()
                    .index(index)
                    .build();
            DeleteIndexResponse response = elasticsearchClient.indices().delete(request);
            return response.acknowledged();
        } catch (Exception e) {
            LOGGER.info("Не удалось удалить индекс " + index);
            throw new RuntimeException(e);
        }
    }

    public long getCountVacancyInIndex(String indexName) {
        CountResponse countResponse;
        try {
            CountRequest countRequest = new CountRequest.Builder()
                    .index(indexName)
                    .build();
            countResponse = elasticsearchClient.count(countRequest);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось посчитать количество записей в индексе " + indexName, e);
        }
        return countResponse.count();
    }

    public void downloadDatFromMongo(MongoCursor<Document> cursor, String index) throws IOException {
        LOGGER.info("Загружаем данные из MongoDB в ElasticSearch с помощью Bulk");
        List<BulkOperation> bulkOperations = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        int count = 0;
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            Optional<Vacancy> vacancy = Optional.empty();
            try {
                vacancy = Optional.of(objectMapper.readValue(doc.toJson(), Vacancy.class));
            } catch (JsonProcessingException e) {
                LOGGER.info("Не удалось распарсить вакансию");
            }
            if (vacancy.isPresent()) {
                Vacancy vacancy1 = vacancy.get();
                bulkOperations.add(new BulkOperation.Builder()
                        .index(i -> i
                                .index(index)
                                .id(String.valueOf(vacancy1.getId()))
                                .document(vacancy1))
                        .build());
            }
            if (bulkOperations.size() == 1000) {
                BulkResponse bulkResponse = elasticsearchClient.bulk(
                        new BulkRequest.Builder().operations(bulkOperations).build()
                );
                if (bulkResponse.errors()) {
                    LOGGER.info("Ошибки при загрузке: " + bulkResponse);
                }
                bulkOperations.clear();
            }
        }
        if (!bulkOperations.isEmpty()) {
            BulkResponse bulkResponse = elasticsearchClient.bulk(
                    new BulkRequest.Builder().operations(bulkOperations).build()
            );
            if (bulkResponse.errors()) {
                System.out.println("Ошибки при загрузке: " + bulkResponse);
            }
        }
    }

    /**
     * Закрываем клиент
     */
    public void close() {
        try {
            restClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

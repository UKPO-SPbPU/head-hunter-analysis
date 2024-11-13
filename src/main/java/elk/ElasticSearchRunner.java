package elk;

import clients.ElasticSearchApiClient;
import clients.MongoDBClient;
import co.elastic.clients.elasticsearch.core.search.Hit;
import collector.utils.DBInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elk.utils.DTO.Vacancy;
import org.bson.Document;

import java.util.List;
import java.util.logging.Logger;

import static collector.utils.PropertiesHelper.loadProperties;

/**
 * Раннер для загрузки данных из Mongo в Elasticsearch
 */
public class ElasticSearchRunner {

    private static final Logger LOGGER = Logger.getLogger(ElasticSearchRunner.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DB_PARSE_2019_PROP_FILE = "mongoparse2019";
    protected static final String DB_PARSE_2024_PROP_FILE = "mongoparse2024";
    protected static final String ELASTIC_SEARCH_PROP_FILE = "elasticsearch";

    private static final String ES_VACANCY_PROP = "index";

    public static void main(String[] args) {
        LOGGER.info("Запускаем раннер для загрузки данных из Mongo в Elasticsearch");
        loadProperties(DB_PARSE_2019_PROP_FILE);
        loadProperties(ELASTIC_SEARCH_PROP_FILE);
        DBInfo dbInfo = new DBInfo();
        MongoDBClient dbClient = new MongoDBClient(dbInfo.getConnectionUrl());
        List<Document> documents = dbClient.getDocuments(
                dbInfo.getDataBase(),
                dbInfo.getCollection(),
                1,
                0
        );
        ElasticSearchApiClient elasticSearchApiClient = new ElasticSearchApiClient();
        String index = System.getProperty(ES_VACANCY_PROP);
        documents.forEach(document -> {
            try {
                Vacancy vacancy = objectMapper.readValue(document.toJson(), Vacancy.class);
                elasticSearchApiClient.addVacancyToIndex(vacancy, index);
                List<Hit<Vacancy>> vacancies = elasticSearchApiClient.getVacancyByFieldText("name", "Веб-дизайнер", index);
                System.out.println();
            } catch (JsonProcessingException e) {
                LOGGER.warning("Не получилось распарсить json объект");
                throw new RuntimeException(e);
            }
        });
        System.out.println();
    }

}

package elk;

import clients.ElasticSearchApiClient;
import clients.MongoDBClient;
import collector.utils.DBInfo;

import java.io.IOException;
import java.util.logging.Logger;

import static collector.utils.PropertiesHelper.loadProperties;

/**
 * Раннер для загрузки данных из Mongo в Elasticsearch
 */
public class ElasticSearchRunner {

    private static final Logger LOGGER = Logger.getLogger(ElasticSearchRunner.class.getName());

    private static final String DB_PARSE_2019_PROP_FILE = "mongoparse2019";
    protected static final String DB_PARSE_2024_PROP_FILE = "mongoparse2024";
    protected static final String ELASTIC_SEARCH_PROP_FILE = "elasticsearch";

    private static final String ES_VACANCY_PROP = "index";

    public static void main(String[] args) {
        LOGGER.info("Запускаем раннер для загрузки данных из Mongo в Elasticsearch");
        loadProperties(ELASTIC_SEARCH_PROP_FILE);
        downloadDataFromDB(DB_PARSE_2019_PROP_FILE);
        downloadDataFromDB(DB_PARSE_2024_PROP_FILE);
    }

    private static void downloadDataFromDB(String dbProperties) {
        loadProperties(dbProperties);

        LOGGER.info("Создаем клиент для работы с MongoDB");
        DBInfo dbInfo = new DBInfo();
        MongoDBClient dbClient = new MongoDBClient(dbInfo.getConnectionUrl());

        LOGGER.info("Создаем клиент для работы с ElasticSearch");
        ElasticSearchApiClient elasticSearchApiClient = new ElasticSearchApiClient();
        final String index = System.getProperty(ES_VACANCY_PROP);

        LOGGER.info("Создаем индекс, если его нет");
        if (!elasticSearchApiClient.isIndexExists(index)) {
            elasticSearchApiClient.createIndex(index);
        }

        LOGGER.info("Загрузка данных из Mongo DB: " + dbInfo.getDataBase()
                + " Collection: " + dbInfo.getCollection()
                + " в ElasticSearch в Index: " + index);
        try (var cursor = dbClient.getCursor(dbInfo.getDataBase(), dbInfo.getCollection())) {
            elasticSearchApiClient.downloadDatFromMongo(cursor, index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            elasticSearchApiClient.close();
            dbClient.close();
        }
        LOGGER.info("Загрузили все данные в индекс " + index);
    }

}

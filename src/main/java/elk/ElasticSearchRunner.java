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
        LOGGER.info("Launching a runner to upload data from Mongo to Elasticsearch");
        loadProperties(ELASTIC_SEARCH_PROP_FILE);
        downloadDataFromDB(DB_PARSE_2019_PROP_FILE);
        downloadDataFromDB(DB_PARSE_2024_PROP_FILE);
    }

    private static void downloadDataFromDB(String dbProperties) {
        loadProperties(dbProperties);

        LOGGER.info("Creating a client for working with MongoDB");
        DBInfo dbInfo = new DBInfo();
        MongoDBClient dbClient = new MongoDBClient(dbInfo.getConnectionUrl());

        LOGGER.info("Creating a client for working with ElasticSearch");
        ElasticSearchApiClient elasticSearchApiClient = new ElasticSearchApiClient();
        final String index = System.getProperty(ES_VACANCY_PROP);

        LOGGER.info("Creating an index if there is none");
        if (!elasticSearchApiClient.isIndexExists(index)) {
            elasticSearchApiClient.createIndex(index);
        }

        LOGGER.info("Downloading data from Mongo DB: " + dbInfo.getDataBase()
                + " Collection: " + dbInfo.getCollection()
                + " in ElasticSearch in the Index: " + index);
        try (var cursor = dbClient.getCursor(dbInfo.getDataBase(), dbInfo.getCollection())) {
            elasticSearchApiClient.downloadDatFromMongo(cursor, index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            elasticSearchApiClient.close();
            dbClient.close();
        }
        LOGGER.info("Uploaded all the data to the index " + index);
    }

}

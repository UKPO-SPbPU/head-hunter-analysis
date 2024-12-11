package collector.utils;

import java.util.logging.Logger;

/**
 * Объект иформации о базе данных
 */
public class DBInfo {

    private static final Logger LOGGER = Logger.getLogger(DBInfo.class.getName());
    private static final String DB_NAME_PROP = "database";
    private static final String COLLECTION_NAME_PROP = "collection";
    private static final String DB_CONNECTION_URL_PROP = "connection_url";

    private final String connectionUrl;
    private final String dataBase;
    private final String collection;

    public DBInfo() {
        LOGGER.info("Initializing DBInfo");
        connectionUrl = System.getProperty(DB_CONNECTION_URL_PROP);
        dataBase = System.getProperty(DB_NAME_PROP);
        collection = System.getProperty(COLLECTION_NAME_PROP);
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public String getDataBase() {
        return dataBase;
    }

    public String getCollection() {
        return collection;
    }

}

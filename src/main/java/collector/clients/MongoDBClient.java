package collector.clients;

import collector.utils.PropertiesHelper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.logging.Logger;

/**
 * Клиент для работы с Mongo
 */
public class MongoDBClient {

    private static final Logger LOGGER = Logger.getLogger(PropertiesHelper.class.getName());
    private final MongoClient mongoClient;

    public MongoDBClient(String connectionUrl) {
        LOGGER.info("Инициализируем MongoDBClient");
        mongoClient = MongoClients.create(connectionUrl);
    }

    public void insertInfoInDB(String info, String dataBase, String collectionName) {
        MongoDatabase database = mongoClient.getDatabase(dataBase);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document doc = new Document("answer", info);
        collection.insertOne(doc);
    }

    public void close() {
        LOGGER.info("Закрываем DB клиент");
        mongoClient.close();
    }

}

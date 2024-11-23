package clients;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Клиент для работы с Mongo
 */
public class MongoDBClient {

    private static final Logger LOGGER = Logger.getLogger(MongoDBClient.class.getName());
    private final MongoClient mongoClient;

    public MongoDBClient(String connectionUrl) {
        LOGGER.info("Инициализируем MongoDBClient");
        mongoClient = MongoClients.create(connectionUrl);
    }

    public void insertInfoInDB(String info, String dataBase, String collectionName) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dataBase)
                .getCollection(collectionName);
        Document doc = new Document("answer", info);
        collection.insertOne(doc);
    }

    public void insertInfoInDB(JSONObject jsonObject, String dataBase, String collectionName) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dataBase)
                .getCollection(collectionName);
        Document doc = Document.parse(jsonObject.toString());
        collection.insertOne(doc);
    }

    public List<Document> getDocuments(String dataBase, String collectionName, int limit, int skip) {
        return mongoClient.getDatabase(dataBase)
                .getCollection(collectionName)
                .find()
                .projection(Projections.exclude("_id"))
                .skip(skip)
                .limit(limit)
                .into(new ArrayList<>());
    }

    public MongoCursor<Document> getCursor(String dataBaseName, String collectionName) {
        return mongoClient.getDatabase(dataBaseName).getCollection(collectionName).find().projection(Projections.exclude("_id")).iterator();
    }

    public void close() {
        LOGGER.info("Закрываем DB клиент");
        mongoClient.close();
    }

}

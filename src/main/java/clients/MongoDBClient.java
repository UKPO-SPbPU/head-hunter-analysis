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
        LOGGER.info("Initializing MongoDBClient");
        mongoClient = MongoClients.create(connectionUrl);
    }

    /**
     * Загружаем данные в Базу данных
     *
     * @param info           - данные
     * @param dataBase       - база данных
     * @param collectionName - коллекция
     */
    public void insertInfoInDB(String info, String dataBase, String collectionName) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dataBase)
                .getCollection(collectionName);
        Document doc = new Document("answer", info);
        collection.insertOne(doc);
    }

    /**
     * Загружаем данные в Базу данных
     *
     * @param jsonObject     - данные
     * @param dataBase       - база данных
     * @param collectionName - коллекция
     */
    public void insertInfoInDB(JSONObject jsonObject, String dataBase, String collectionName) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dataBase)
                .getCollection(collectionName);
        Document doc = Document.parse(jsonObject.toString());
        collection.insertOne(doc);
    }

    /**
     * Возвращает документы из коллекции
     *
     * @param dataBase       - база данных
     * @param collectionName - коллекция
     * @param limit          - максимальное количество
     * @param skip           - количество пропускаемых документов с начала
     *
     * @return - лист докуметнов
     */
    public List<Document> getDocuments(String dataBase, String collectionName, int limit, int skip) {
        return mongoClient.getDatabase(dataBase)
                .getCollection(collectionName)
                .find()
                .projection(Projections.exclude("_id"))
                .skip(skip)
                .limit(limit)
                .into(new ArrayList<>());
    }

    /**
     * Возвращает итераратор по коллекции
     *
     * @param dataBase       - база данных
     * @param collectionName - коллекция
     *
     * @return - итератор
     */
    public MongoCursor<Document> getCursor(String dataBase, String collectionName) {
        return mongoClient.getDatabase(dataBase).getCollection(collectionName).find()
                .projection(Projections.exclude("_id")).iterator();
    }

    /**
     * Возвращае количество документов в коллекции
     *
     * @param dataBase       - база данных
     * @param collectionName - коллекция
     * @return количество документов
     */
    public long getCountDocument(String dataBase, String collectionName) {
        return mongoClient.getDatabase(dataBase).getCollection(collectionName).countDocuments();
    }

    /**
     * Закрваем подключение к базе данных
     */
    public void close() {
        LOGGER.info("Closing the DB client");
        mongoClient.close();
    }

}

package parser;

import collector.clients.MongoDBClient;
import collector.utils.DBInfo;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import parser.utils.Fields;
import parser.utils.JSONFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static collector.utils.PropertiesHelper.loadProperties;

/**
 * Запуск парсера вакансии из поля answer и загрузки данныхв в Mongo
 */
public class ParserRunner {

    private static final Logger LOGGER = Logger.getLogger(ParserRunner.class.getName());

    private static final String DB_PARSE_2019_PROP_FILE = "mongoparse2019";
    private static final String DB_2019_PROP_FILE = "mongo2019";
    protected static final String DB_PARSE_2024_PROP_FILE = "mongoparse2024";
    private static final String DB_2024_PROP_FILE = "mongo2024";
    private static final int ONCE_COUNT_DOCUMENTS = 1;
    private static final int ONCE_MILLION_DOCUMENTS_IN_COLLECTION = 8;
    private static final String ANSWER_FIELD = "answer";

    public static void main(String[] args) {
        loadProperties(DB_2024_PROP_FILE);

        LOGGER.info("Запускаи парсер данных из Mongo");
        DBInfo dbInfo = new DBInfo();
        List<JSONObject> vacancies = new ArrayList<>();
        MongoDBClient dbClient = new MongoDBClient(dbInfo.getConnectionUrl());
        for (int i = 0; i < ONCE_MILLION_DOCUMENTS_IN_COLLECTION * 1000000 / ONCE_COUNT_DOCUMENTS; i++) {
            dbClient.getDocuments(dbInfo.getDataBase(), dbInfo.getCollection(), ONCE_COUNT_DOCUMENTS, i * ONCE_COUNT_DOCUMENTS)
                    .forEach(document -> vacancies.add(new JSONObject(String.valueOf(document.get(ANSWER_FIELD)))));
            loadProperties(DB_PARSE_2024_PROP_FILE);
            final DBInfo dbInfo2 = new DBInfo();
            vacancies.forEach(vac -> {
                JSONObject vacNewJson = JSONFilter.filter(vac);
                dbClient.insertInfoInDB(vacNewJson, dbInfo2.getDataBase(), dbInfo2.getCollection());
            });
        }
        dbClient.close();
    }

}

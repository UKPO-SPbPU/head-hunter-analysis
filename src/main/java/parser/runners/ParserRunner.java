package parser.runners;

import clients.MongoDBClient;
import collector.utils.DBInfo;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import parser.utils.FieldsFilter;
import parser.utils.ITFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static collector.utils.PropertiesHelper.loadProperties;

/**
 * Парсер вакансий из одной коллекции Mongo в другую
 *
 * 1. Берет часть вакансий из коллекции со всеми вакансиями
 * 2. Парсит JSON поля "answer"
 * 3. Фильтрует по professional_roles для выялвение IT-вакансий
 * 3. Берет нужные поля из JSON и кладет в другую коллекцию
 */
public class ParserRunner {

    private static final Logger LOGGER = Logger.getLogger(ParserRunner.class.getName());

    private static final String DB_PARSE_2019_PROP_FILE = "mongoparse2019";
    private static final String DB_2019_PROP_FILE = "mongo2019";
    protected static final String DB_PARSE_2024_PROP_FILE = "mongoparse2024";
    private static final String DB_2024_PROP_FILE = "mongo2024";
    private static final int ONCE_COUNT_DOCUMENTS = 25_000;
    private static final String ANSWER_FIELD = "answer";

    public static void main(String[] args) {
        LOGGER.info("Запускаем парсер данных из Mongo");
        parseData(DB_2019_PROP_FILE, DB_PARSE_2019_PROP_FILE);
        parseData(DB_2024_PROP_FILE, DB_PARSE_2024_PROP_FILE);
    }

    private static void parseData(String properties1, String properties2) {
        loadProperties(properties1);
        final DBInfo dbInfo = new DBInfo();
        MongoDBClient dbClient = new MongoDBClient(dbInfo.getConnectionUrl());
        long countDocument = dbClient.getCountDocument(dbInfo.getDataBase(), dbInfo.getCollection());
        for (int i = 0; i <= countDocument / ONCE_COUNT_DOCUMENTS; i++) {
            LOGGER.info("Начали фильтрацию от " + i * ONCE_COUNT_DOCUMENTS + " до " + (i + 1) * ONCE_COUNT_DOCUMENTS);

            LOGGER.info("Выгружаем данные и парсим");
            List<JSONObject> vacancies = new ArrayList<>();
            List<Document> documents = dbClient.getDocuments(
                    dbInfo.getDataBase(),
                    dbInfo.getCollection(),
                    ONCE_COUNT_DOCUMENTS,
                    i * ONCE_COUNT_DOCUMENTS
            );
            documents.forEach(document -> {
                try {
                    vacancies.add(new JSONObject(String.valueOf(document.get(ANSWER_FIELD))));
                } catch (JSONException e) {
                    System.out.println("НЕ СМОГЛИ РАСПАРСИТЬ: " + document.get(ANSWER_FIELD));
                }
            });

            LOGGER.info("Фильтруем по professional_roles");
            loadProperties(properties2);
            final DBInfo dbInfo2 = new DBInfo();
            List<JSONObject> iTVacancies = vacancies.stream().filter(new ITFilter()).toList();

            LOGGER.info("Фильтруем поля");
            List<JSONObject> iTVacanciesCorrectFields = iTVacancies.stream().map(FieldsFilter::filter).toList();

            LOGGER.info("Загружаем " + iTVacanciesCorrectFields.size() + " вакансий");
            iTVacanciesCorrectFields.forEach(vac -> dbClient.insertInfoInDB(vac, dbInfo2.getDataBase(), dbInfo2.getCollection()));
        }
        dbClient.close();
    }

}

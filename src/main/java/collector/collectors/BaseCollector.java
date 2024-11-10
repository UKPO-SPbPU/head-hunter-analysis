package collector.collectors;

import collector.clients.HeadHunterApiClient;
import collector.clients.MongoDBClient;
import collector.utils.DBInfo;
import collector.utils.URLBuilder;
import collector.utils.URLS;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Общие данные для коллекторов данных из HH
 */
abstract class BaseCollector {

    protected static final Logger LOGGER = Logger.getLogger(BaseCollector.class.getName());
    protected static final HeadHunterApiClient HH_CLIENT = new HeadHunterApiClient();
    protected static final URLBuilder URL_BUILDER = new URLBuilder(URLS.VACANCIES.getUrl());

    abstract public void start(String startId, String endId, DBInfo dbInfo, int maxCountVacancies);

    protected static void downloadVacanciesFromHHToDBbyIds(List<Integer> idsVacanciesList, DBInfo dbInfo) {
        LOGGER.info("\nDB_URL = " + dbInfo.getConnectionUrl() +
                "\nDATABASE = " + dbInfo.getDataBase() +
                "\nCOLLECTION = " + dbInfo.getCollection() + "\n");
        MongoDBClient dbClient = new MongoDBClient(dbInfo.getConnectionUrl());
        idsVacanciesList.parallelStream()
                .forEach(number ->
                {
                    String url = URL_BUILDER.addPartAndGet(String.valueOf(number));
                    try {
                        String response = HH_CLIENT.doRequset(url);
                        if (!response.equals("NULL")) {
                            dbClient.insertInfoInDB(response, dbInfo.getDataBase(), dbInfo.getCollection());
                        }
                    } catch (IOException e) {
                        LOGGER.info("Не удалось выполнить запрос с id = " + number);
                    }
                });
        dbClient.close();
    }

}

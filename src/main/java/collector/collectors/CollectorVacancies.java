package collector.collectors;

import clients.HeadHunterApiClient;
import clients.MongoDBClient;
import collector.utils.DBInfo;
import collector.utils.URLBuilder;
import collector.utils.URLS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Коллектор для сбора инф-ии о вакансиях с HH в MongoDB
 */
public class CollectorVacancies {

    private static final Logger LOGGER = Logger.getLogger(CollectorVacancies.class.getName());
    private static final HeadHunterApiClient HH_CLIENT = new HeadHunterApiClient();
    private static final URLBuilder URL_BUILDER = new URLBuilder(URLS.VACANCIES.getUrl());
    private static final int MAX_COUNT_FOR_ONE_DOWNLOAD = 100000;

    public void start(String startId, String endId, DBInfo dbInfo) {
        LOGGER.info("Job uploads start with id = " + startId);
        int start = Integer.parseInt(startId);
        int end = Integer.parseInt(endId);
        for (int i = 0; start + i <= end
                || start + (i - MAX_COUNT_FOR_ONE_DOWNLOAD) <= end; i += MAX_COUNT_FOR_ONE_DOWNLOAD) {
            int countVacancies = start + i <= end
                    ? MAX_COUNT_FOR_ONE_DOWNLOAD
                    : start - end;
            List<Integer> numbersVacanciesList = new ArrayList<>();
            for (int j = i; start + j <= end && j <= countVacancies; j++) {
                numbersVacanciesList.add(Integer.parseInt(startId) + j);
            }
            downloadVacanciesFromHHToDBbyIds(numbersVacanciesList, dbInfo);
        }
        LOGGER.info("We have completed uploading vacancies to the database ");
    }

    private static void downloadVacanciesFromHHToDBbyIds(List<Integer> idsVacanciesList, DBInfo dbInfo) {
        LOGGER.info("\nDB_URL = " + dbInfo.getConnectionUrl() +
                "\nDATABASE = " + dbInfo.getDataBase() +
                "\nCOLLECTION = " + dbInfo.getCollection() + "\n");
        MongoDBClient dbClient = new MongoDBClient(dbInfo.getConnectionUrl());
        idsVacanciesList.parallelStream()
                .forEach(number -> {
                    String url = URL_BUILDER.addPartAndGet(String.valueOf(number));
                    try {
                        String response = HH_CLIENT.doRequset(url);
                        if (!response.equals("NULL")) {
                            dbClient.insertInfoInDB(response, dbInfo.getDataBase(), dbInfo.getCollection());
                        }
                    } catch (IOException e) {
                        LOGGER.info("Failed to execute request with id = " + number);
                    }
                });
        dbClient.close();
    }

}

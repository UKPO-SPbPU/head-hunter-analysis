package collector;

import collector.clients.HeadHunterApiClient;
import collector.clients.MongoDBClient;
import collector.utils.DBInfo;
import collector.utils.URLBuilder;
import collector.utils.URLS;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Коллектор для сбора инф-ии о вакансиях с HH в MongoDB
 */
public class Collector {

    private static final Logger LOGGER = Logger.getLogger(Collector.class.getName());
    private static final HeadHunterApiClient HH_CLIENT = new HeadHunterApiClient();
    private static final URLBuilder URL_BUILDER = new URLBuilder(URLS.VACANCIES.getUrl());

    public static void start(String startId, String endId, DBInfo dbInfo) {
        LOGGER.info("Коллектор начинает работу с id = " + startId +
                "\nDB_URL = " + dbInfo.getConnectionUrl() +
                "\nDATABASE = " + dbInfo.getDataBase() +
                "\nCOLLECTION = " + dbInfo.getCollection() + "\n");
        AtomicInteger startIdAtomic = new AtomicInteger(Integer.parseInt(startId));
        AtomicInteger endIdAtomic = new AtomicInteger(Integer.parseInt(endId));
        MongoDBClient dbClient = new MongoDBClient(dbInfo.getConnectionUrl());

        String url = URL_BUILDER.addPartAndGet(startIdAtomic.toString());
        String response = null;
        try {
            response = HH_CLIENT.doRequset(url);
        } catch (IOException e) {
            LOGGER.info("Не удалось выполнить запрос с id = " + startIdAtomic);
        }
        do {
            if (!Objects.equals(response, "NULL")) {
                dbClient.insertInfoInDB(response, dbInfo.getDataBase(), dbInfo.getCollection());
            }
            startIdAtomic.incrementAndGet();
            url = URL_BUILDER.addPartAndGet(startIdAtomic.toString());
            try {
                response = HH_CLIENT.doRequset(url);
            } catch (Exception e) {
                LOGGER.info("Не удалось выполнить запрос с id = " + startIdAtomic);
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                LOGGER.info("Не удалось поставить на паузу поток");
            }
        } while (startIdAtomic.get() <= endIdAtomic.get());
        dbClient.close();
        LOGGER.info("Завершили программу");
    }

}

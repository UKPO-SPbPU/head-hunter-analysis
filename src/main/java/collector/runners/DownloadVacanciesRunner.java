package collector.runners;

import collector.collectors.CollectorVacancies;
import collector.utils.DBInfo;

import java.util.logging.Logger;

import static collector.utils.PropertiesHelper.loadProperties;
import static java.lang.System.getProperty;

/**
 * Раннер для загрузки вакансий
 */
public class DownloadVacanciesRunner {

    private static final Logger LOGGER = Logger.getLogger(DownloadVacanciesRunner.class.getName());

    private static final String HH_PROP_FILE = "headhunter";
    private static final String DB_2019_PROP_FILE = "mongo2019";
    private static final String DB_2024_PROP_FILE = "mongo2024";
    private static final String START_ID_PROP = "start_id";
    private static final String END_ID_PROP = "end_id";

    public static void main(String[] args) {
        LOGGER.info("We are starting to upload vacancies for 2019 and 2024 ");
        loadProperties(HH_PROP_FILE);
        download(DB_2024_PROP_FILE);
        download(DB_2019_PROP_FILE);
    }

    private static void download(String properties) {
        loadProperties(properties);
        new CollectorVacancies().start(getProperty(START_ID_PROP), getProperty(END_ID_PROP), new DBInfo());
    }

}

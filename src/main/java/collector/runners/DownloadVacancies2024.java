package collector.runners;

import collector.collectors.CollectorVacancies2024;
import collector.utils.DBInfo;

import static collector.utils.PropertiesHelper.loadProperties;
import static java.lang.System.getProperty;

/**
 * Раннер для загрузки вакансий за 2024 год
 */
public class DownloadVacancies2024 extends BaseRunner {

    public static void main(String[] args) {
        loadProperties(HH_PROP_FILE);
        loadProperties(DB_2024_PROP_FILE);
        new CollectorVacancies2024().start(getProperty(START_ID_PROP), getProperty(END_ID_PROP), new DBInfo(), 1000000);
    }

}

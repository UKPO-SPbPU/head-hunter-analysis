import collector.Collector;
import collector.utils.DBInfo;

import static collector.utils.PropertiesHelper.loadProperties;
import static java.lang.System.getProperty;


public class Main {

    private static final String HH_PROP_FILE = "headhunter";
    private static final String DB_PROP_FILE = "mongo";
    private static final String START_ID_PROP = "start_id";
    private static final String END_ID_PROP = "end_id";

    public static void main(String[] args) {
        loadProperties(HH_PROP_FILE);
        loadProperties(DB_PROP_FILE);
        Collector.start(getProperty(START_ID_PROP), getProperty(END_ID_PROP), new DBInfo());
    }

}

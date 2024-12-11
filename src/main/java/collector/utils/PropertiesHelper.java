package collector.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Хелпер для пропертей
 */
public class PropertiesHelper {

    private static final Logger LOGGER = Logger.getLogger(PropertiesHelper.class.getName());
    private static final String FILE_NAME = "src/main/resources/%s.properties";

    public static void loadProperties(String propertiesFile) {
        LOGGER.info("Download the properties from the file " + propertiesFile);
        Properties properties = new Properties();
        try (FileInputStream inStream = new FileInputStream(String.format(FILE_NAME, propertiesFile))) {
            properties.load(inStream);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("The file could not be found " + propertiesFile, e);
        } catch (IOException e) {
            throw new IllegalStateException("Generic IO error with the file " + propertiesFile, e);
        }
        properties.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
    }

}

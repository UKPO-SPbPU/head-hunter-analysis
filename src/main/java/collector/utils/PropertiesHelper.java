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

    public static void loadProperties(String propertiesFile) {
        LOGGER.info("Загрузим проперти из файла " + propertiesFile);
        Properties properties = new Properties();
        try (FileInputStream inStream = new FileInputStream("src/main/resources/" + propertiesFile + ".properties")) {
            properties.load(inStream);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Не удалось найти файл" + propertiesFile, e);
        } catch (IOException e) {
            throw new IllegalStateException("Generic IO error с файлом " + propertiesFile, e);
        }
        properties.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
    }

}

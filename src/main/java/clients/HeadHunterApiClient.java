package clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * ApiClient для работы с HH
 * https://api.hh.ru/openapi/redoc
 */
public class HeadHunterApiClient {

    private static final Logger LOGGER = Logger.getLogger(HeadHunterApiClient.class.getName());

    protected static final String USER_AGENT_PROP = "user_agent";
    protected static final String TOKEN_PROP = "token";

    public String doRequset(String stringUrl) throws IOException {
        LOGGER.info("Отправляем запрос на HH: " + stringUrl);
        HttpURLConnection conn = getHttpURLConnection(stringUrl);
        StringBuilder response = new StringBuilder();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } else {
            LOGGER.warning("Ззапрос не сработал. Код ответа: " + conn.getResponseCode() + " URL " + stringUrl);
            conn.disconnect();
            return "NULL";
        }
        conn.disconnect();
        return response.toString();
    }

    private HttpURLConnection getHttpURLConnection(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", System.getProperty(USER_AGENT_PROP));
        conn.setRequestProperty("Authorization", "Bearer " + System.getProperty(TOKEN_PROP));
        return conn;
    }

}

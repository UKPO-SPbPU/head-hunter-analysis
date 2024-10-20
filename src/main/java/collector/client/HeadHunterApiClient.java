package collector.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ApiClient для работы с HH
 * https://api.hh.ru/openapi/redoc
 */
public class HeadHunterApiClient {

    public void doRequset(String stringUrl) throws IOException {
        HttpURLConnection conn = getHttpURLConnection(stringUrl);
        if (validateResponseCode(conn)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Response: " + response);
        } else {
            System.out.println("GET запрос не сработал. Код ответа: " + conn.getResponseCode());
        }
    }

    private HttpURLConnection getHttpURLConnection(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        return conn;
    }

    private boolean validateResponseCode(HttpURLConnection conn) throws IOException {
        return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
    }

}

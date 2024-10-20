import collector.client.HeadHunterApiClient;
import collector.client.utils.URLBuilder;
import collector.client.utils.URLS;

import java.util.concurrent.atomic.AtomicLong;


public class Main {

    private static AtomicLong startId = new AtomicLong(23878600);

    public static void main(String[] args) throws Exception {
        URLBuilder urlBuilder = new URLBuilder(URLS.VACANCIES.getUrl());
        String url = urlBuilder.addPartOfUrl(startId.toString()).build();
        new HeadHunterApiClient().doRequset(url);
    }

}

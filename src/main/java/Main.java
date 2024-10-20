import client.HeadHunterApiClient;
import parameters.AreaConverter;
import utils.URLBuilder;
import utils.URLS;


public class Main {

    private static final AreaConverter AREA_CONVERTER = new AreaConverter();

    public static void main(String[] args) throws Exception {
        URLBuilder urlBuilder = new URLBuilder(URLS.VACANCIES.getUrl());
        String url = urlBuilder.addAreaId(AREA_CONVERTER.convert("Москва"))
                .addNumberPage(1)
                .addPerPage(20)
                .addText("scala")
                .build();
        new HeadHunterApiClient().doRequset(url);
    }

}

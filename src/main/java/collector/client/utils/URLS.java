package collector.client.utils;

public enum URLS {

    BASE("https://api.hh.ru/"),
    VACANCIES(BASE.getUrl() + "vacancies");

    private final String url;

    URLS(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}

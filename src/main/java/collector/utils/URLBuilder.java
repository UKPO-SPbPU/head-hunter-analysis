package collector.utils;

/**
 * Билдер для URL
 */
public class URLBuilder {

    private final StringBuilder baseUrl = new StringBuilder();

    public URLBuilder(String baseUrl) {
        this.baseUrl.append(baseUrl);
    }

    public String addPartAndGet(String part) {
        return new StringBuilder(baseUrl).append("/").append(part).toString();
    }

}

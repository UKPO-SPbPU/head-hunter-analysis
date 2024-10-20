package collector.client.utils;

/**
 * Билдер для URL
 */
public class URLBuilder {

    private StringBuilder baseUrl = new StringBuilder();

    public URLBuilder(String baseUrl) {
        this.baseUrl.append(baseUrl);
    }

    public URLBuilder addParameter(String key, String value) {
        baseUrl.append("&").append(key).append("=").append(value);
        return this;
    }

    public URLBuilder addPartOfUrl(String part) {
        baseUrl.append("/").append(part);
        return this;
    }

    public String build() {
        return baseUrl.toString();
    }

}

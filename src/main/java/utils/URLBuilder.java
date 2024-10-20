package utils;

/**
 * Билдер для URL
 */
public class URLBuilder {

    private StringBuilder baseUrl = new StringBuilder();

    public URLBuilder(String baseUrl) {
        this.baseUrl.append(baseUrl + "?");
    }

    public URLBuilder addAreaId(int areaId) {
        baseUrl.append("area=").append(areaId).append("&");
        return this;
    }

    public URLBuilder addNumberPage(int page) {
        baseUrl.append("page=").append(page).append("&");
        return this;
    }

    public URLBuilder addPerPage(int perPage) {
        baseUrl.append("per_page=").append(perPage).append("&");
        return this;
    }

    public URLBuilder addText(String text) {
        baseUrl.append("text=").append(text).append("&");
        return this;
    }

    public URLBuilder onlyWithSalary() {
        if (baseUrl.toString().contains("only_with_salary")) {
            return this;
        }
        baseUrl.append("only_with_salary=true&");
        return this;
    }

    public URLBuilder addSalary(int salary) {
        if (salary <= 0) {
            return this;
        }
        baseUrl.append("salary=").append(salary).append("&");
        return this;
    }

    public String build() {
        return baseUrl.toString();
    }

}

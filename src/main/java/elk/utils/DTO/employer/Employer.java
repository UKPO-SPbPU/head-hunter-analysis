package elk.utils.DTO.employer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Представление поля employer в Vacancy.java
 */
@Data
public class Employer {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("url")
    private String url;

    @JsonProperty("alternate_url")
    private String alternateUrl;

    @JsonProperty("vacancies_url")
    private String vacanciesUrl;

    @JsonProperty("accredited_it_employer")
    private Boolean accreditedItEmployer;

    @JsonProperty("trusted")
    private Boolean trusted;

    @JsonProperty("logo_urls")
    private LogoUrls logoUrls;

}

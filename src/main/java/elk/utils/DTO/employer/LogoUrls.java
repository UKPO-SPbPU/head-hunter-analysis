package elk.utils.DTO.employer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Представление поля logo_urls в Employer.java
 */
@Data
public class LogoUrls {

    @JsonProperty("90")
    private String url90;

    @JsonProperty("240")
    private String url240;

    @JsonProperty("original")
    private String original;

}

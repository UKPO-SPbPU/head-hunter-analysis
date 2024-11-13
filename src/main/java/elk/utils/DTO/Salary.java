package elk.utils.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Представление поля salary в Vacancy.java
 */
@Data
public class Salary {

    @JsonProperty("gross")
    private Boolean gross;

    @JsonProperty("from")
    private Integer from;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("to")
    private Integer to;

}

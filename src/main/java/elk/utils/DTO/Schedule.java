package elk.utils.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Представление поля schedule в Vacancy.java
 */
@Data
public class Schedule {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}

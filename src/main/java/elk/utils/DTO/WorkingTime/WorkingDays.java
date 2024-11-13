package elk.utils.DTO.WorkingTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Представление поля working_days в Vacancy.java
 */
@Data
public class WorkingDays {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}

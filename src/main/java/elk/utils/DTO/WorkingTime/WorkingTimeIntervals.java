package elk.utils.DTO.WorkingTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Представление поля working_time_intervals в Vacancy.java
 */
@Data
public class WorkingTimeIntervals {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}

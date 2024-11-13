package elk.utils.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Представление поля experience в Vacancy.java
 */
@Data
public class Experience {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}

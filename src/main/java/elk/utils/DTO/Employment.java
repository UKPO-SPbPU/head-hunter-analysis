package elk.utils.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Представление поля employment в Vacancy.java
 */
@Data
public class Employment {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}

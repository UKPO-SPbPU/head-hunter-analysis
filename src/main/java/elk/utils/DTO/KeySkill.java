package elk.utils.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Представление поля key_skills в Vacancy.java
 */
@Data
public class KeySkill {

    @JsonProperty("name")
    private String name;

}

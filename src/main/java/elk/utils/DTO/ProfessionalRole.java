package elk.utils.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Представление поля professional_roles в Vacancy.java
 */
@Data
public class ProfessionalRole {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

}

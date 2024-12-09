package elk.utils.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import elk.utils.DTO.WorkingTime.WorkingDays;
import elk.utils.DTO.WorkingTime.WorkingTimeIntervals;
import elk.utils.DTO.WorkingTime.WorkingTimeModes;
import elk.utils.DTO.employer.Employer;
import lombok.Data;

import java.util.List;

/**
 * Представление вакансии из коллекции hh-it-parse-vacancies-20**
 */
@Data
public class Vacancy {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("area")
    private Area area;

    @JsonProperty("key_skills")
    private List<KeySkill> keySkills;

    @JsonProperty("professional_roles")
    private List<ProfessionalRole> professionalRoles;

    @JsonProperty("working_days")
    private List<WorkingDays> workingDays;

    @JsonProperty("working_time_intervals")
    private List<WorkingTimeIntervals> workingTimeIntervals;

    @JsonProperty("working_time_modes")
    private List<WorkingTimeModes> workingTimeModes;

    @JsonProperty("published_at")
    private String publishedAt;

    @JsonProperty("initial_created_at")
    private String initialCreatedAt;

    @JsonProperty("accept_handicapped")
    private Boolean acceptHandicapped;

    @JsonProperty("accept_temporary")
    private Boolean accept_temporary;

    @JsonProperty("description")
    private String description;

    @JsonProperty("employment")
    private Employment employment;

    @JsonProperty("experience")
    private Experience experience;

    @JsonProperty("employer")
    private Employer employer;

    @JsonProperty("salary")
    private Salary salary;

    @JsonProperty("schedule")
    private Schedule schedule;

    public Schedule getSchedule() {
        return schedule;
    }

    public Salary getSalary() {
        return salary;
    }

    public Employer getEmployer() {
        return employer;
    }

    public Experience getExperience() {
        return experience;
    }

    public Employment getEmployment() {
        return employment;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getAccept_temporary() {
        return accept_temporary;
    }

    public Boolean getAcceptHandicapped() {
        return acceptHandicapped;
    }

    public String getInitialCreatedAt() {
        return initialCreatedAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public List<WorkingTimeModes> getWorkingTimeModes() {
        return workingTimeModes;
    }

    public List<WorkingTimeIntervals> getWorkingTimeIntervals() {
        return workingTimeIntervals;
    }

    public List<WorkingDays> getWorkingDays() {
        return workingDays;
    }

    public List<ProfessionalRole> getProfessionalRoles() {
        return professionalRoles;
    }

    public List<KeySkill> getKeySkills() {
        return keySkills;
    }

    public Area getArea() {
        return area;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}

package parser.utils;

public enum Fields {

    ID("id"),
    NAME("name"),
    DESCRIPTION("description"),
    EMPLOYER("employer"),
    EMPLOYMENT("employment"),
    EXPERIENCE("experience"),
    ACCEPT_HANDICAPPED("accept_handicapped"),
    ACCEPT_TEMPORARY("accept_temporary"),
    AREA("area"),
    INITIAL_CREATED_AT("initial_created_at"),
    PUBLISHED_AT("published_at"),
    KEY_SKILLS("key_skills"),
    PROFESSIONAL_ROLES("professional_roles"),
    SALARY("salary"),
    SCHEDULE("schedule"),
    WORKING_DAYS("working_days"),
    WORKING_TIME_INTERVALS("working_time_intervals"),
    WORKING_TIME_MODES("working_time_modes"),
    INDUSTRY("industry");

    private final String field;

    Fields(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

}

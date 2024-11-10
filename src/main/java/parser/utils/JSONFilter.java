package parser.utils;


import org.json.JSONObject;

import static parser.utils.Fields.ID;
import static parser.utils.Fields.NAME;
import static parser.utils.Fields.DESCRIPTION;
import static parser.utils.Fields.EMPLOYER;
import static parser.utils.Fields.EMPLOYMENT;
import static parser.utils.Fields.EXPERIENCE;
import static parser.utils.Fields.ACCEPT_HANDICAPPED;
import static parser.utils.Fields.ACCEPT_TEMPORARY;
import static parser.utils.Fields.AREA;
import static parser.utils.Fields.INITIAL_CREATED_AT;
import static parser.utils.Fields.PUBLISHED_AT;
import static parser.utils.Fields.KEY_SKILLS;
import static parser.utils.Fields.PROFESSIONAL_ROLES;
import static parser.utils.Fields.SALARY;
import static parser.utils.Fields.SCHEDULE;
import static parser.utils.Fields.WORKING_DAYS;
import static parser.utils.Fields.WORKING_TIME_INTERVALS;
import static parser.utils.Fields.WORKING_TIME_MODES;

/**
 * Фильтр JSONObject на основе Fields.java
 */
public class JSONFilter {

    public static JSONObject filter(JSONObject jsonObject) {
        JSONObject newJsonObjet = new JSONObject();
        newJsonObjet.put(ID.getField(), jsonObject.getString(ID.getField()));
        newJsonObjet.put(NAME.getField(), jsonObject.getString(NAME.getField()));
        newJsonObjet.put(DESCRIPTION.getField(), jsonObject.getString(DESCRIPTION.getField()));
        newJsonObjet.put(EMPLOYER.getField(), jsonObject.getJSONObject(EMPLOYER.getField()));
        newJsonObjet.put(EMPLOYMENT.getField(), jsonObject.getJSONObject(EMPLOYMENT.getField()));
        newJsonObjet.put(EXPERIENCE.getField(), jsonObject.getJSONObject(EXPERIENCE.getField()));
        newJsonObjet.put(ACCEPT_HANDICAPPED.getField(), jsonObject.getBoolean(ACCEPT_HANDICAPPED.getField()));
        newJsonObjet.put(ACCEPT_TEMPORARY.getField(), jsonObject.getBoolean(ACCEPT_TEMPORARY.getField()));
        newJsonObjet.put(AREA.getField(), jsonObject.getJSONObject(AREA.getField()));
        newJsonObjet.put(INITIAL_CREATED_AT.getField(), jsonObject.getString(INITIAL_CREATED_AT.getField()));
        newJsonObjet.put(PUBLISHED_AT.getField(), jsonObject.getString(PUBLISHED_AT.getField()));
        newJsonObjet.put(KEY_SKILLS.getField(), jsonObject.getJSONArray(KEY_SKILLS.getField()));
        newJsonObjet.put(PROFESSIONAL_ROLES.getField(), jsonObject.getJSONArray(PROFESSIONAL_ROLES.getField()));
        newJsonObjet.put(SALARY.getField(), jsonObject.getJSONObject(SALARY.getField()));
        newJsonObjet.put(SCHEDULE.getField(), jsonObject.getJSONObject(SCHEDULE.getField()));
        newJsonObjet.put(WORKING_DAYS.getField(), jsonObject.getJSONArray(WORKING_DAYS.getField()));
        newJsonObjet.put(WORKING_TIME_INTERVALS.getField(), jsonObject.getJSONArray(WORKING_TIME_INTERVALS.getField()));
        newJsonObjet.put(WORKING_TIME_MODES.getField(), jsonObject.getJSONArray(WORKING_TIME_MODES.getField()));
        return newJsonObjet;
    }

}

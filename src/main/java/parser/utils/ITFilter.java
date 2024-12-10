package parser.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Фильтр JSON вакансии по professional_roles для отбора IT-вакансий
 */
public class ITFilter implements Predicate<JSONObject> {

    // Роли только из сферы Информационных технологий
    private static final List<Integer> IDS_IT_PROFESSIONAL_ROLES = Arrays.asList(
            156, 160, 10, 12, 150, 25, 165, 34, 36, 73, 155, 96, 164,
            104, 157, 107, 112, 113, 148, 114, 116, 121, 124, 125, 126, 84
    );

    @Override
    public boolean test(JSONObject jsonObject) {
        JSONArray professionalRoles = jsonObject.getJSONArray(Fields.PROFESSIONAL_ROLES.getField());
        for (int i = 0; i < professionalRoles.length(); i++) {
            if (IDS_IT_PROFESSIONAL_ROLES.contains(professionalRoles.getJSONObject(i).getInt(Fields.ID.getField()))) {
                return true;
            }
        }
        return false;
    }

}

package collector.collectors;

import collector.utils.DBInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Коллектор для сбора инф-ии о вакансиях с HH в MongoDB за 2019 год
 */
public class CollectorVacancies2019 extends BaseCollector {

    @Override
    public void start(String startId, String endId, DBInfo dbInfo, int maxCountVacancies) {
        LOGGER.info("Коллектор начинает работу с id = " + startId);
        List<Integer> numbersVacanciesList = new ArrayList<>();
        int countVacancies = Integer.parseInt(endId) - Integer.parseInt(startId);
        for (int i = 0;
             Integer.parseInt(startId) + i <= Integer.parseInt(endId)
                     && i <= countVacancies
                    && i < maxCountVacancies;
             i++) {
            numbersVacanciesList.add(Integer.parseInt(startId) + i);
        }
        downloadVacanciesFromHHToDBbyIds(numbersVacanciesList, dbInfo);
        LOGGER.info("Завершили программу");
    }

}
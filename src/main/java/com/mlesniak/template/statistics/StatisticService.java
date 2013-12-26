package com.mlesniak.template.statistics;

import com.mlesniak.template.dao.StatisticsDao;
import com.mlesniak.template.model.StatisticDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class StatisticService {
    private static Logger log = LoggerFactory.getLogger(StatisticService.class);
    private static StatisticService INSTANCE;

    public static StatisticService get() {
        if (INSTANCE == null) {
            INSTANCE = new StatisticService();
        }

        return INSTANCE;
    }

    public static <T> T collect(StatisticCategory category, String description, Callable<T> resultType) {
        long start;
        long end;

        T result = null;
        try {
            start = System.currentTimeMillis();
            result = resultType.call();
            end = System.currentTimeMillis();
        }catch (RuntimeException e) {
            log.warn("Throwing runtime exception. e=" + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Is this the correct thing to do?
            log.error("Caught exception. e=" + e.getMessage(), e);
            return null;
        }

        long time = end - start;
        log.info("Statistics. category=" + category.toString() + ", description='" + description + "', time=" + time);

        StatisticDO statisticDO = new StatisticDO();
        statisticDO.setCategory(category);
        statisticDO.setDescription(description);
        statisticDO.setTime(time);
        StatisticsDao.get().write(statisticDO);

        return result;
    }
}

package com.mlesniak.template.jobs;

import com.mlesniak.template.config.Config;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerService {
    public static final String REPEAT_SECONDS = ".repeatSeconds";
    private Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private static SchedulerService INSTANCE;

    private static final String START_NOW = ".startNow";

    public static SchedulerService get() {
        if (INSTANCE == null) {
            INSTANCE = new SchedulerService();
        }

        return INSTANCE;
    }

    public void startScheduler() {
        Config config = Config.get();

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            for (Class<? extends Job> jobClass : findJobs()) {
                String name = jobClass.getCanonicalName();
                log.debug("Working on job " + name);
                JobDetail job = newJob(jobClass)
                        .withIdentity(name)
                        .build();

                TriggerBuilder<Trigger> builder = newTrigger().withIdentity(name);
                if (config.getBoolean(name + START_NOW)) {
                    builder.startNow();
                    log.debug("Starting immediately");
                }

                if (config.isKeyDefined(name + REPEAT_SECONDS)) {
                    int seconds = config.getInt(name + REPEAT_SECONDS);
                    builder.withSchedule(simpleSchedule()
                            .withIntervalInSeconds(seconds)
                            .repeatForever());
                    log.debug("Repeating forever. seconds=" + seconds);
                }

                scheduler.scheduleJob(job, builder.build());
                log.info("Scheduled job. job=" + name);
            }

            log.info("Scheduler started");
        } catch (SchedulerException e) {
            log.error("Unable to start scheduling. msg=" + e.getMessage(), e);
        }
    }

    private Set<Class<? extends Job>> findJobs() {
        Reflections reflections = new Reflections("com.mlesniak");
        Set<Class<? extends Job>> jobs = reflections.getSubTypesOf(Job.class);
        if (log.isDebugEnabled()) {
            for (Class<? extends Job> job : jobs) {
                log.debug("Job found:" + job.getCanonicalName());
            }
        }
        return jobs;
    }

    public void stopScheduler() {
        try {
            StdSchedulerFactory.getDefaultScheduler().shutdown(false);
            log.info("Scheduler shut down.");
        } catch (SchedulerException e) {
            log.error("Unable to shutdown scheduler. msg=" + e.getMessage(), e);
        }
    }
}

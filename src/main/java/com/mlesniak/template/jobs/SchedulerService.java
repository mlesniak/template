package com.mlesniak.template.jobs;

import com.mlesniak.template.config.Config;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


public class SchedulerService {
    private Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private static SchedulerService INSTANCE;

    private static final String START_NOW = ".startNow";
    private static final String REPEAT_SECONDS = ".repeatSeconds";
    private static final String CRON = ".cron";
    private Set<Class<? extends Job>> jobs;

    public static SchedulerService get() {
        if (INSTANCE == null) {
            INSTANCE = new SchedulerService();
        }

        return INSTANCE;
    }

    /**
     * Returns true if the given key corresponds to a job class.
     */
    public boolean isJobKey(String key) {
        for (Class<? extends Job> jobClass : findJobs()) {
            if (key.startsWith(jobClass.getCanonicalName())) {
                return true;
            }
        }

        return false;
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

                if (config.isKeyDefined(name + START_NOW) && config.getBoolean(name + START_NOW)) {
                    log.debug("Starting immediately");
                    builder.startNow();
                }

                if (config.isKeyDefined(name + REPEAT_SECONDS)) {
                    int seconds = config.getInt(name + REPEAT_SECONDS);
                    log.debug("Repeating forever. seconds=" + seconds);
                    builder.withSchedule(simpleSchedule()
                            .withIntervalInSeconds(seconds)
                            .repeatForever());
                }

                if (config.isKeyDefined(name + CRON)) {
                    String cronExpression = config.get(name + CRON);
                    if (cronExpression.startsWith("#")) {
                        log.info("Cron expression is disabled. job=" + name);
                    } else {
                        log.debug("With cron. cron=" + cronExpression);
                        builder.withSchedule(cronSchedule(cronExpression));
                    }
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
        if (jobs != null) {
            return jobs;
        }

        Reflections reflections = new Reflections("com.mlesniak");
        jobs = reflections.getSubTypesOf(Job.class);
        if (log.isDebugEnabled()) {
            for (Class<? extends Job> job : jobs) {
                log.debug("Job found:" + job.getCanonicalName());
            }
        }
        jobs = Collections.unmodifiableSet(jobs);

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

    public void restartScheduler() {
        stopScheduler();
        startScheduler();
    }
}

package com.mlesniak.template;

import com.mlesniak.template.app.HomePage;
import com.mlesniak.template.authentification.BasicAuthenticationSession;
import com.mlesniak.template.authentification.SignInPage;
import com.mlesniak.template.authentification.SignOutPage;
import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigPage;
import com.mlesniak.template.dao.UserDao;
import com.mlesniak.template.errorpage.AccessDeniedPage;
import com.mlesniak.template.errorpage.InternalErrorPage;
import com.mlesniak.template.jobs.HelloJob;
import com.mlesniak.template.logging.LogPage;
import com.mlesniak.template.statistic.StatisticPage;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.settings.IExceptionSettings;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;

public class WicketApplication extends AuthenticatedWebApplication {
    private Logger log = LoggerFactory.getLogger(WicketApplication.class);
    private static Map<String, Class<? extends WebPage>> pageMapping = new HashMap<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            StdSchedulerFactory.getDefaultScheduler().shutdown(false);
            log.info("Scheduler shut down.");
        } catch (SchedulerException e) {
            log.error("Unable to shutdown scheduler. msg=" + e.getMessage(), e);
        }

    }

    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return BasicAuthenticationSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return SignInPage.class;
    }

    @Override
    public void init() {
        super.init();
        log.info("Starting application.");
        Config.get().init();
        UserDao.get().addAdminUser();

        getApplicationSettings().setAccessDeniedPage(AccessDeniedPage.class);
        getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);
        // show internal error page rather than default developer page
        getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);

        mountPages();

        if (getConfigurationType() == RuntimeConfigurationType.DEPLOYMENT) {
            getResourceSettings().setThrowExceptionOnMissingResource(false);
        }

        //EmailService.get().sendEmail("mail@mlesniak.com", "Test", new Date().toString());

        startScheduler();
    }

    private void startScheduler() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            Reflections reflections = new Reflections("com.mlesniak");
            Set<Class<? extends Job>> jobs = reflections.getSubTypesOf(Job.class);
            for (Class<? extends Job> job : jobs) {
                log.info("Job found:" + job.getCanonicalName());
            }

            JobDetail job = newJob(HelloJob.class)
                    .withIdentity("job1", "group1")
                    .build();

//            // Trigger the job to run now, and then repeat every 40 seconds
//            Trigger trigger = newTrigger()
//                    .withIdentity("trigger1", "group1")
//                    .startNow()
//                    .withSchedule(simpleSchedule()
//                            .withIntervalInSeconds(40)
//                            .repeatForever())
//                    .build();
//
//            // Tell quartz to schedule the job using our trigger
//            scheduler.scheduleJob(job, trigger);

            log.info("Scheduler started");
        } catch (SchedulerException e) {
            log.error("Unable to start scheduling. msg=" + e.getMessage(), e);
        }
    }

    private void mountPages() {
        Map<String, Class<? extends WebPage>> mapping = getPageMapping();
        for (String path : mapping.keySet()) {
            mountPage(path, mapping.get(path));
        }
    }

    private static void initializePageMapping() {
        pageMapping.put("/config", ConfigPage.class);
        pageMapping.put("/log", LogPage.class);
        pageMapping.put("/login", SignInPage.class);
        pageMapping.put("/logout", SignOutPage.class);
        pageMapping.put("/statistic", StatisticPage.class);
    }

    public static Map<String, Class<? extends WebPage>> getPageMapping() {
        if (pageMapping.isEmpty()) {
            initializePageMapping();
        }

        return Collections.unmodifiableMap(pageMapping);
    }
}

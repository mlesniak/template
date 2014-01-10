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
import com.mlesniak.template.jobs.SchedulerService;
import com.mlesniak.template.logging.LogPage;
import com.mlesniak.template.plugin.HelloWorld;
import com.mlesniak.template.plugin.PluginService;
import com.mlesniak.template.statistic.StatisticPage;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WicketApplication extends AuthenticatedWebApplication {
    private Logger log = LoggerFactory.getLogger(WicketApplication.class);
    private static Map<String, Class<? extends WebPage>> pageMapping = new HashMap<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SchedulerService.get().stopScheduler();
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

        // In case of unhandled exception redirect it to a custom page.
        getRequestCycleListeners().add(new AbstractRequestCycleListener() {
            @Override
            public IRequestHandler onException(RequestCycle cycle, Exception e) {
                return new RenderPageRequestHandler(new
                        PageProvider(new InternalErrorPage(e)));
            }
        });

        mountPages();

        if (getConfigurationType() == RuntimeConfigurationType.DEPLOYMENT) {
            getResourceSettings().setThrowExceptionOnMissingResource(false);
        }

        //EmailService.get().sendEmail("mail@mlesniak.com", "Test", new Date().toString());
        //SMSService.get().sendSMS(Config.get().get(ConfigKeys.SMS_ADMIN), "Test with ä!?&");

        SchedulerService.get().startScheduler();

        HelloWorld world = PluginService.get().getPlugin(HelloWorld.class);
        world.sayHello("Michael");
//        PluginService.get().storeJARinDatabase(HelloWorld.class);
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

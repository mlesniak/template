package com.mlesniak.template;

import com.mlesniak.template.app.HomePage;
import com.mlesniak.template.authentification.BasicAuthenticationSession;
import com.mlesniak.template.authentification.SignOutPage;
import com.mlesniak.template.authentification.SignInPage;
import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigPage;
import com.mlesniak.template.dao.UserDao;
import com.mlesniak.template.email.EmailService;
import com.mlesniak.template.errorpage.AccessDeniedPage;
import com.mlesniak.template.errorpage.InternalErrorPage;
import com.mlesniak.template.logging.LogPage;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.settings.IExceptionSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WicketApplication extends AuthenticatedWebApplication {
    private Logger log = LoggerFactory.getLogger(WicketApplication.class);

    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass(){
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

        mountPage("/config", ConfigPage.class);
        mountPage("/log", LogPage.class);
        mountPage("/login", SignInPage.class);
        mountPage("/logout", SignOutPage.class);

        EmailService.get().sendEmail();
    }
}

package com.mlesniak.template.app;

import com.mlesniak.template.auth.BasicAuthenticationSession;
import com.mlesniak.template.auth.LogoutPage;
import com.mlesniak.template.config.Config;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
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

        mountPage("/config", ConfigPage.class);
        mountPage("/log", LogPage.class);
        mountPage("/login", SignInPage.class);
        mountPage("/logout", LogoutPage.class);
    }
}

package com.mlesniak.template.auth;

import com.mlesniak.template.app.HomePage;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutPage extends WebPage {
    private Logger log = LoggerFactory.getLogger(LogoutPage.class);

    public LogoutPage() {
        AuthenticatedWebSession authenticatedWebSession = AuthenticatedWebSession.get();
        authenticatedWebSession.invalidate();
        authenticatedWebSession.signOut();
        setResponsePage(HomePage.class);
        log.info("User logged out. username=" + ((BasicAuthenticationSession) authenticatedWebSession).getUsername());
    }
}

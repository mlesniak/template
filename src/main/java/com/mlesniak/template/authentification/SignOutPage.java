package com.mlesniak.template.authentification;

import com.mlesniak.template.app.HomePage;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignOutPage extends WebPage {
    private Logger log = LoggerFactory.getLogger(SignOutPage.class);

    public SignOutPage() {
        AuthenticatedWebSession authenticatedWebSession = AuthenticatedWebSession.get();
        authenticatedWebSession.invalidate();
        authenticatedWebSession.signOut();
        setResponsePage(HomePage.class);
        log.info("User logged out. username=" + ((BasicAuthenticationSession) authenticatedWebSession).getUsername());
    }
}

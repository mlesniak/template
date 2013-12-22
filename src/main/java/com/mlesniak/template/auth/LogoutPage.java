package com.mlesniak.template.auth;

import com.mlesniak.template.app.HomePage;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;

public class LogoutPage extends WebPage {
    public LogoutPage() {
        AuthenticatedWebSession.get().invalidate();
        AuthenticatedWebSession.get().signOut();
        setResponsePage(HomePage.class);
    }
}

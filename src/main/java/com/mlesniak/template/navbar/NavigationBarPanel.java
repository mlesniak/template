package com.mlesniak.template.navbar;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public class NavigationBarPanel extends Panel {
    public NavigationBarPanel(String id) {
        super(id);

        WebMarkupContainer adminMenu = new WebMarkupContainer("adminMenu") {
            @Override
            public boolean isVisible() {
                AuthenticatedWebSession session = AuthenticatedWebSession.get();
                return session.isSignedIn() && session.getRoles().hasRole("ADMIN");
            }
        };
        add(adminMenu);

        WebMarkupContainer loginMenu= new WebMarkupContainer("loginMenu") {
            @Override
            public boolean isVisible() {
                return !AuthenticatedWebSession.get().isSignedIn();
            }
        };
        add(loginMenu);

        WebMarkupContainer logoutnMenu= new WebMarkupContainer("logoutMenu") {
            @Override
            public boolean isVisible() {
                return AuthenticatedWebSession.get().isSignedIn();
            }
        };
        add(logoutnMenu);
    }
}

package com.mlesniak.template.navbar;

import com.mlesniak.template.auth.BasicAuthenticationSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

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

        WebMarkupContainer loginMenu = new WebMarkupContainer("loginMenu") {
            @Override
            public boolean isVisible() {
                return !AuthenticatedWebSession.get().isSignedIn();
            }
        };
        add(loginMenu);

        WebMarkupContainer logoutMenu = new WebMarkupContainer("logoutMenu") {
            @Override
            public boolean isVisible() {
                return AuthenticatedWebSession.get().isSignedIn();
            }
        };
        add(logoutMenu);

        add(new Label("loggedInAs", new Model<String>() {
            @Override
            public String getObject() {
                return getString("panel.loggedInAs") + " " +
                        ((BasicAuthenticationSession) AuthenticatedWebSession.get()).getUsername();
            }
        }) {
            @Override
            public boolean isVisible() {
                return AuthenticatedWebSession.get().isSignedIn();
            }
        });

        // add to logout container
    }
}

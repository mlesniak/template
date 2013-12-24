package com.mlesniak.template.navbar;

import com.mlesniak.template.auth.BasicAuthenticationSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class NavigationBarPanel extends Panel {
    public NavigationBarPanel(String id) {
        super(id);

        addLoginMenu();
        addLoggedInMenu();
        addAdminMenu();
        addGravatarImage();
    }

    private void addGravatarImage() {
        WebMarkupContainer container = new WebMarkupContainer("gravatar");
        add(container);

        BasicAuthenticationSession session = (BasicAuthenticationSession) getSession();
        if (session.isSignedIn()) {
            String email = session.getUser().getEmail();
            if (email == null) {
                email = "";
            }

            String url = "http://www.gravatar.com/avatar/" + DigestUtils.md5Hex(email) + "?s=30";
            container.add(new AttributeModifier("src", new Model<>(url)));
        }
    }

    private void addLoggedInMenu() {
        add(new Label("loggedInAs", new Model<String>() {
            @Override
            public String getObject() {
                return ((BasicAuthenticationSession) AuthenticatedWebSession.get()).getUsername();
            }
        }) {
            @Override
            public boolean isVisible() {
                return AuthenticatedWebSession.get().isSignedIn();
            }
        });
    }

    private void addAdminMenu() {
        WebMarkupContainer adminMenu = new WebMarkupContainer("adminMenu") {
            @Override
            public boolean isVisible() {
                AuthenticatedWebSession session = AuthenticatedWebSession.get();
                return session.isSignedIn() && session.getRoles().hasRole("ADMIN");
            }
        };
        add(adminMenu);
    }

    private void addLoginMenu() {
        WebMarkupContainer loginMenu = new WebMarkupContainer("loginMenu") {
            @Override
            public boolean isVisible() {
                return !AuthenticatedWebSession.get().isSignedIn();
            }
        };
        add(loginMenu);
    }
}

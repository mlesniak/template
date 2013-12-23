package com.mlesniak.template.auth;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicAuthenticationSession extends AuthenticatedWebSession {
    private Logger log = LoggerFactory.getLogger(BasicAuthenticationSession.class);
    private String username;

    public BasicAuthenticationSession(Request request) {
        super(request);
    }

    @Override
    public boolean authenticate(String username, String password) {
        boolean result = username.equals(password);
        if (result) {
            this.username = username;
        }
        return result;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Roles getRoles() {
        Roles resultRoles = new Roles();

        if (isSignedIn()) {
            resultRoles.add(Roles.USER);
        }

        if (username != null && username.equals("m")) {
            resultRoles.add(Roles.ADMIN);
        }

        return resultRoles;
    }
}
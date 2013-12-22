package com.mlesniak.template.auth;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

public class BasicAuthenticationSession extends AuthenticatedWebSession {
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
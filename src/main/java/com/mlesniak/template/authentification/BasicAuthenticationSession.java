package com.mlesniak.template.authentification;

import com.mlesniak.template.dao.UserDao;
import com.mlesniak.template.model.UserDO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class BasicAuthenticationSession extends AuthenticatedWebSession {
    private Logger log = LoggerFactory.getLogger(BasicAuthenticationSession.class);
    private UserDO user;

    public BasicAuthenticationSession(Request request) {
        super(request);
    }

    @Override
    public boolean authenticate(String username, String password) {
        UserDO user = UserDao.get().getByUsername(username);
        if (user == null) {
            return false;
        }

        boolean result = DigestUtils.md5Hex(username + password).equals(user.getPassword());
        if (result) {
            this.user = user;
            String language = "en'";
            if (user.getLanguage() != null) {
                language = user.getLanguage();
            }
            setLocale(new Locale(language));
        }
        return result;
    }

    @Override
    public Roles getRoles() {
        Roles resultRoles = new Roles();

        if (isSignedIn()) {
            for (String role : user.getRoles().split(",")) {
                resultRoles.add(role);
            }
        }

        return resultRoles;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public UserDO getUser() {
        return user;
    }
}
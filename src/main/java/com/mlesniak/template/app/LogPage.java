package com.mlesniak.template.app;

import com.mlesniak.template.logging.LogPanel;
import com.mlesniak.template.navbar.NavigationBarPanel;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AuthorizeInstantiation("ADMIN")
public class LogPage extends BasePage {
    private Logger log = LoggerFactory.getLogger(LogPage.class);

    public LogPage(final PageParameters parameters) {
        super(parameters);

        // For I18N-testing.
        // getSession().setLocale(Locale.GERMANY);

        LogPanel logPanel = new LogPanel("logPanel");
        add(logPanel);

        add(new NavigationBarPanel("navigationBar"));
    }
}

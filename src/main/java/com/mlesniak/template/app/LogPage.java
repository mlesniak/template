package com.mlesniak.template.app;

import com.mlesniak.template.logging.LogPanel;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AuthorizeInstantiation("ADMIN")
public class LogPage extends WebPage {
    private Logger log = LoggerFactory.getLogger(LogPage.class);

    public LogPage(final PageParameters parameters) {
        super(parameters);

        // For I18N-testing.
        // getSession().setLocale(Locale.GERMANY);

        LogPanel logPanel = new LogPanel("logPanel");
        add(logPanel);
    }
}

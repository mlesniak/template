package com.mlesniak.template.logging;

import com.mlesniak.template.BasePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AuthorizeInstantiation("ADMIN")
public class LogPage extends BasePage {
    private Logger log = LoggerFactory.getLogger(LogPage.class);

    public LogPage(final PageParameters parameters) {
        super(parameters);

        LogPanel logPanel = new LogPanel("logPanel");
        add(logPanel);
    }
}

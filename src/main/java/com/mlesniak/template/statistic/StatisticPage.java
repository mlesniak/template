package com.mlesniak.template.statistic;

import com.mlesniak.template.BasePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AuthorizeInstantiation("ADMIN")
public class StatisticPage extends BasePage {
    private Logger log = LoggerFactory.getLogger(StatisticPage.class);

    public StatisticPage(final PageParameters parameters) {
        super(parameters);

        // For I18N-testing.
        // getSession().setLocale(Locale.GERMANY);

        StatisticPanel logPanel = new StatisticPanel("statisticPanel");
        add(logPanel);
    }
}

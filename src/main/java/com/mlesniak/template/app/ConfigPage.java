package com.mlesniak.template.app;

import com.mlesniak.template.config.ConfigPanel;
import com.mlesniak.template.navbar.NavigationBarPanel;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AuthorizeInstantiation("ADMIN")
public class ConfigPage extends BasePage {
    private Logger log = LoggerFactory.getLogger(ConfigPage.class);

    public ConfigPage(final PageParameters parameters) {
        super(parameters);

        // For I18N-testing.
        // getSession().setLocale(Locale.GERMANY);

        ConfigPanel configPanel = new ConfigPanel("configPanel");
        add(configPanel);

        add(new NavigationBarPanel("navigationBar"));
    }
}

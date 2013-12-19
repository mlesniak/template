package com.mlesniak.template.app;

import com.mlesniak.template.config.ConfigPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigPage extends WebPage {
    private Logger log = LoggerFactory.getLogger(ConfigPage.class);

    public ConfigPage(final PageParameters parameters) {
        super(parameters);

        // For I18N-testing.
        // getSession().setLocale(Locale.GERMANY);

        ConfigPanel configPanel = new ConfigPanel("configPanel");
        add(configPanel);
    }
}

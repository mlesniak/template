package com.mlesniak.template.plugin;

import com.mlesniak.template.BasePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AuthorizeInstantiation("ADMIN")
public class PluginPage extends BasePage {
    private Logger log = LoggerFactory.getLogger(PluginPage.class);

    public PluginPage(final PageParameters parameters) {
        super(parameters);

        PluginPanel configPanel = new PluginPanel("configPanel");
        add(configPanel);
    }
}

package com.mlesniak.template;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {
    public HomePage(final PageParameters parameters) {
        super(parameters);
        add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
    }
}

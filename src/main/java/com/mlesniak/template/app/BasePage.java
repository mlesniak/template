package com.mlesniak.template.app;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BasePage extends WebPage {
    public BasePage() {
        addPageTitle();
    }

    public BasePage(PageParameters parameters) {
        super(parameters);
        addPageTitle();
    }

    private void addPageTitle() {
        String key = getClass().getSimpleName() + ".title";
        add(new Label("pageTitle", new StringResourceModel(key, this, null)));
    }
}

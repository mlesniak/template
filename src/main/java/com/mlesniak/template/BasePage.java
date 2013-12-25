package com.mlesniak.template;

import com.mlesniak.template.navigation.NavigationBarPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BasePage extends WebPage {
    public BasePage() {
        addPageTitle();
        addNavigationBar();
    }

    public BasePage(PageParameters parameters) {
        super(parameters);
        addPageTitle();
        addNavigationBar();
    }

    private void addNavigationBar() {
        add(new NavigationBarPanel("navigationBar"));
    }

    private void addPageTitle() {
        String key = getClass().getSimpleName() + ".title";
        add(new Label("pageTitle", new StringResourceModel(key, this, null)));

    }
}

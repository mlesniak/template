package com.mlesniak.template;

import com.mlesniak.template.navigation.NavigationBarPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
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

        add(new AjaxFallbackLink<Void>("fakeAjax") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                // Ignored.
            }
        });
    }

    private void addNavigationBar() {
        add(new NavigationBarPanel("navigationBar"));
    }

    private void addPageTitle() {
        String key = getClass().getSimpleName() + ".title";
        add(new Label("pageTitle", new StringResourceModel(key, this, null)));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings()
                .getJQueryReference()));
    }
}

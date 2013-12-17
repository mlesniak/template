package com.mlesniak.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigPage extends WebPage {
    private Logger log = LoggerFactory.getLogger(ConfigPage.class);

    public ConfigPage(PageParameters parameters) {
        super(parameters);

        // Model from Config values to HashMap.
        // Form
        // Backing model for Form


        Form<ConfigModel> form = new Form<ConfigModel>("configForm", new ConfigModel());

        ListView<Config.Key> listView = new ListView<Config.Key>("configValues", Config.get().getDefinedKeys()) {
            @Override
            protected void populateItem(final ListItem<Config.Key> item) {
                item.add(new Label("key", item.getModel().getObject().get()));
                item.add(new TextField<String>("value", new Model<String>() {
                    @Override
                    public String getObject() {
                        return Config.get().get(item.getModelObject());
                    }

                    @Override
                    public void setObject(String newValue) {
                        Config.get().set(item.getModelObject(), newValue);
                    }
                }));
            }
        };
        listView.setReuseItems(true);

        form.add(listView);
        add(form);
    }
}

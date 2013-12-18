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

import java.util.*;

public class ConfigPage extends WebPage {
    private Logger log = LoggerFactory.getLogger(ConfigPage.class);

    public ConfigPage(PageParameters parameters) {
        super(parameters);

        final Map<Config.Key,String> model = Config.get().getConfig();
        Form form = createForm(model);
        add(form);
    }

    private Form createForm(final Map<Config.Key, String> model) {
        Form form = new Form("configForm") {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                for (Map.Entry<Config.Key, String> entry : model.entrySet()) {
                    Config.get().set(entry.getKey(), entry.getValue());
                    log.info("Storing new value. " + entry.getKey() + ": " + entry.getValue());
                }
            }
        };

        ListView<Config.Key> formFields = createFormFields(model);
        form.add(formFields);
        return form;
    }

    private ListView<Config.Key> createFormFields(final Map<Config.Key, String> model) {
        ListView<Config.Key> listView = new ListView<Config.Key>("configValues", computeListModel(model)) {
            @Override
            protected void populateItem(final ListItem<Config.Key> item) {
                item.add(new Label("key", item.getModelObject().get()));
                item.add(new TextField<>("value", new Model<String>() {
                    @Override
                    public String getObject() {
                        return model.get(item.getModelObject());
                    }

                    @Override
                    public void setObject(String newValue) {
                        model.put(item.getModelObject(), newValue);
                    }
                }));
            }
        };
        listView.setReuseItems(true);
        return listView;
    }

    private List<Config.Key> computeListModel(Map<Config.Key, String> model) {
        List<Config.Key> keys = new ArrayList<>(model.keySet());
        Collections.sort(keys, new Comparator<Config.Key>() {
            @Override
            public int compare(Config.Key o1, Config.Key o2) {
                return o1.get().compareTo(o2.get());
            }
        });
        return keys;
    }
}

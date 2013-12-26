package com.mlesniak.template.config;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ConfigPanel extends Panel {
    private Logger log = LoggerFactory.getLogger(ConfigPanel.class);

    public ConfigPanel(String id) {
        super(id);

        final Map<Config.Key, String> model = Config.get().getConfig();
        Form form = createForm(model);
        add(form);

        addResetButton(model, form);
    }

    private Form createForm(final Map<Config.Key, String> model) {
        Form form = new Form("configForm") {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                boolean reload = false;
                for (Map.Entry<Config.Key, String> entry : model.entrySet()) {
                    if (isChangeableKey(entry.getKey())) {
                        if (entry.getKey().equals(Config.Key.showDefaultOptions)) {
                            reload = true;
                        }

                        Config.get().set(entry.getKey(), entry.getValue());
                        log.info("Storing new value. " + entry.getKey() + ": " + entry.getValue());
                    }
                }

                if (reload) {
                    setResponsePage(getPage().getClass());
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
                Label label = new Label("key", item.getModelObject().get());
                TextField<String> inputField = new TextField<>("value", new Model<String>() {
                    @Override
                    public String getObject() {
                        return model.get(item.getModelObject());
                    }

                    @Override
                    public void setObject(String newValue) {
                        model.put(item.getModelObject(), newValue);
                    }
                });

                try {
                    String documentation = getString(item.getModelObject().get());
                    if (documentation != null) {
                        inputField.add(new AttributeModifier("title", documentation));
                    }
                } catch (MissingResourceException e) {
                    // This is ok.
                }

                // handleAutoFocusOnFirstElement(item, inputField);
                handleDatabaseDisabeled(item, label, inputField);
                item.add(inputField);
                item.add(label);
            }

            private void handleDatabaseDisabeled(ListItem<Config.Key> item, Label label, TextField<String> inputField) {
                if (!isChangeableKey(item.getModelObject())) {
                    inputField.setEnabled(false);
                    label.add(new AttributeAppender("class", "disabled"));
                    item.setVisible(Config.get().getBoolean(Config.Key.showDefaultOptions));
                }
            }

            private void handleAutoFocusOnFirstElement(ListItem<Config.Key> item, TextField<String> inputField) {
                if (item.getIndex() == 0) {
                    inputField.add(new AttributeModifier("autofocus", "true"));
                }
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

    private boolean isChangeableKey(Config.Key key) {
        return !key.get().startsWith("database.");
    }

    private void addResetButton(final Map<Config.Key, String> model, final Form form) {
        AjaxFallbackButton resetButton = new AjaxFallbackButton("resetButton", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                log.info("Resetting configuration.");
                Config.get().resetToConfigFile();
                setResponsePage(getPage().getClass());
                model.clear();
            }
        };
        form.add(resetButton);
    }
}

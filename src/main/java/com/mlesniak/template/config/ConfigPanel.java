package com.mlesniak.template.config;

import com.mlesniak.template.jobs.SchedulerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
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
    private TextField<String> filter;
    private ListView<String> formFields;
    private final Form form;
    private ListView<String> listView;
    private String oldKeyword = "";

    public ConfigPanel(String id) {
        super(id);
        final Map<String, String> model = Config.get().getConfig();

        form = createConfigKeyList(model);
        add(form);
        addFilter(model);
        addResetButton(model, form);
    }

    private void addFilter(final Map<String, String> model) {
        Form filterForm = new Form("filterForm");

        final TextField<String> keyword = new TextField<>("filter", Model.of(""));
        keyword.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // Check that the onChange-event was not simply passed due to tabbing.
                if (StringUtils.equals(keyword.getModelObject(), oldKeyword)) {
                    return;
                }
                oldKeyword = keyword.getModelObject();

                target.add(form);
                listView.setModelObject(computeListModel(model));
                target.appendJavaScript("updateQtip();");
                target.appendJavaScript("updateTabIndex();");
            }
        });

        filterForm.add(keyword);
        add(filterForm);
        keyword.setOutputMarkupId(true);
        filter = keyword;
    }

    private Form createConfigKeyList(final Map<String, String> model) {
        Form form = new Form("configForm") {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                boolean reload = false;
                boolean restartScheduler = false;
                for (Map.Entry<String, String> entry : model.entrySet()) {
                    if (isChangeableKey(entry.getKey())) {
                        if (Config.get().get(entry.getKey()).equals(entry.getValue())) {
                            continue;
                        }

                        if (entry.getKey().equals(ConfigKeys.SHOW_DEFAULT_OPTIONS)) {
                            reload = true;
                        }
                        if (SchedulerService.get().isJobKey(entry.getKey())) {
                            // Compare with old value and only restart if changed.
                            restartScheduler = true;
                        }

                        Config.get().set(entry.getKey(), entry.getValue());
                        log.info("Storing new value. " + entry.getKey() + ": " + entry.getValue());
                    }
                }

                if (restartScheduler) {
                    SchedulerService.get().restartScheduler();
                }

                if (reload) {
                    setResponsePage(getPage().getClass());
                }
            }
        };

        formFields = createFormFields(model);
        form.add(formFields);
        form.setOutputMarkupId(true);
        return form;
    }

    private ListView<String> createFormFields(final Map<String, String> model) {
        listView = new ListView<String>("configValues", computeListModel(model)) {
            int tabIndex = 1;

            @Override
            protected void onBeforeRender() {
                super.onBeforeRender();
                tabIndex = 1;
            }

            @Override
            protected void populateItem(final ListItem<String> item) {
                Label label = new Label("key", item.getModelObject());
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
                    String documentation = getString(item.getModelObject());
                    if (documentation != null) {
                        inputField.add(new AttributeModifier("title", documentation));
                    }
                } catch (MissingResourceException e) {
                    // This is ok.
                }

                handleDatabaseDisabeled(item, label, inputField);
                item.add(inputField);
                item.add(label);
            }

            /**
             * Returns true if visible.
             */
            private void handleDatabaseDisabeled(ListItem<String> item, Label label, TextField<String> inputField) {
                if (!isChangeableKey(item.getModelObject())) {
                    inputField.setEnabled(false);
                    label.add(new AttributeAppender("class", "disabled"));
                    item.setVisible(Config.get().getBoolean(ConfigKeys.SHOW_DEFAULT_OPTIONS));
                }
            }
        };
        listView.setReuseItems(false);
        listView.setOutputMarkupId(true);
        return listView;
    }

    private List<String> computeListModel(Map<String, String> model) {
        List<String> keys = new ArrayList<>(model.keySet());
        Collections.sort(keys);
        final String input = filter == null ? "" : (filter.getModelObject() == null ? "" : filter.getModelObject());
        CollectionUtils.filter(keys, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                String s = (String) object;
                return StringUtils.containsIgnoreCase(s, input);
            }
        });

        return keys;
    }

    private boolean isChangeableKey(String key) {
        return !key.startsWith("database.");
    }

    private void addResetButton(final Map<String, String> model, final Form form) {
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

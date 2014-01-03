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

    public ConfigPanel(String id) {
        super(id);

        final Map<String, String> model = Config.get().getConfig();
        form = createForm(model);
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
                target.add(form);
                listView.setModelObject(computeListModel(model));
                System.out.println("changed:" + keyword.getModelObject());
                target.appendJavaScript("updateQtip();");
            }
        });

        filterForm.add(keyword);
        add(filterForm);
        keyword.setOutputMarkupId(true);
        filter = keyword;
    }

    private Form createForm(final Map<String, String> model) {
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
            @Override
            protected void onBeforeRender() {
                super.onBeforeRender();
                System.out.println("RENDERING");
            }

            @Override
            protected void populateItem(final ListItem<String> item) {
                System.out.println("Populating for item=" + item.getModelObject());

//                if (!StringUtils.containsIgnoreCase(item.getModelObject(), filter.getModelObject())) {
//                    return;
//                }

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

                // handleAutoFocusOnFirstElement(item, inputField);
                inputField.add(new AttributeModifier("tabindex", Integer.toString(item.getIndex() - 1)));
                handleDatabaseDisabeled(item, label, inputField);
                item.add(inputField);
                item.add(label);
            }

            private void handleDatabaseDisabeled(ListItem<String> item, Label label, TextField<String> inputField) {
                if (!isChangeableKey(item.getModelObject())) {
                    inputField.setEnabled(false);
                    label.add(new AttributeAppender("class", "disabled"));
                    item.setVisible(Config.get().getBoolean(ConfigKeys.SHOW_DEFAULT_OPTIONS));
                }
            }

            private void handleAutoFocusOnFirstElement(ListItem<String> item, TextField<String> inputField) {
                if (item.getIndex() == 0) {
                    inputField.add(new AttributeModifier("autofocus", "true"));
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
        System.out.println("filtering for " + input);
        CollectionUtils.filter(keys, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                String s = (String) object;
                if (StringUtils.containsIgnoreCase(s, input)) {
                    System.out.println("accepting " + s);
                }

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

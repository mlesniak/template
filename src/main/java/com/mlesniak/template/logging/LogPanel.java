package com.mlesniak.template.logging;

import ch.qos.logback.classic.Level;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class LogPanel extends Panel {
    private Logger log = LoggerFactory.getLogger(LogPanel.class);
    private static List<Level> availableLevel = availableLogLevels();
    private List<LogDO> logDOs = new LinkedList<>();

    public LogPanel(String id) {
        super(id);

        final LogModel model = new LogModel();
        Form<LogModel> form = new Form<>("logForm", new CompoundPropertyModel<>(model));

        final TextField<String> keyword = new TextField<>("keyword");
        keyword.setOutputMarkupId(true);
        form.add(keyword);

        IModel logs = new LoadableDetachableModel<List<LogDO>>() {
            @Override
            protected List<LogDO> load() {
                return logDOs;
            }
        };

        final ListView<LogDO> listView = new ListView<LogDO>("listview", logs) {
            @Override
            protected void populateItem(ListItem<LogDO> item) {
                item.add(new Label("message", item.getModelObject().getFormattedMessages()));
            }
        };

        final WebMarkupContainer container = new WebMarkupContainer("view");
        container.add(listView);
        container.setOutputMarkupId(true);
        add(container);

        DropDownChoice<Level> level = new DropDownChoice<>("level", availableLevel);
        model.setLevel(Level.ALL);
        form.add(level);

        AjaxFallbackButton button = new AjaxFallbackButton("submit", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                handleSubmit(model);
                target.add(container);
                target.focusComponent(keyword);
            }
        };
        form.add(button);

        add(form);
    }

    public void handleSubmit(LogModel model) {
        logDOs = LogDao.get().getLogByFilter(modelToLogFilter(model));
        System.out.println(logDOs.size());
    }

    private LogFilter modelToLogFilter(LogModel model) {
        LogFilter logFilter = LogFilter.start();
        logFilter.addKeyword(model.getKeyword());
        logFilter.addLevel(model.getLevel());
        logFilter.build();
        System.out.println(logFilter);
        return logFilter;
    }

    private static List<Level> availableLogLevels() {
        List<Level> list = new LinkedList<>();
        list.add(Level.ALL);
        list.add(Level.ERROR);
        list.add(Level.WARN);
        list.add(Level.INFO);
        list.add(Level.DEBUG);
        list.add(Level.TRACE);
        return list;
    }
}

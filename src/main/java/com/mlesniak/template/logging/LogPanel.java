package com.mlesniak.template.logging;

import ch.qos.logback.classic.Level;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.behavior.AttributeAppender;
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
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class LogPanel extends Panel implements Serializable {
    private Logger log = LoggerFactory.getLogger(LogPanel.class);
    private static List<Level> availableLevel = availableLogLevels();
    private List<LogDO> logDOs = new LinkedList<>();

    private ThreadLocal<SimpleDateFormat> simpleDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private ThreadLocal<SimpleDateFormat> simpleTimeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss.SSS");
        }
    };

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
                LogDO model = item.getModelObject();
                item.add(new Label("eventid", model.getId()));
                item.add(new Label("date", toDate(model.getTimestamp())));
                item.add(new Label("time", toTime(model.getTimestamp())));
                item.add(new Label("level", model.getLevel()));
                item.add(new Label("message", model.getFormattedMessages()));

                if (model.getLevel().equals(Level.WARN.toString())) {
                    item.add(new AttributeAppender("class", new Model<String>("warning"), " "));
                }
                if (model.getLevel().equals(Level.ERROR.toString())) {
                    item.add(new AttributeAppender("class", new Model<String>("danger"), " "));
                }
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

    private String toDate(long timestamp) {
        return simpleDateFormat.get().format(new Date(timestamp));
    }

    private String toTime(long timestamp) {
        return simpleTimeFormat.get().format(new Date(timestamp));
    }
}

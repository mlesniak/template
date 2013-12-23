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

    private transient ThreadLocal<SimpleDateFormat> simpleDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private transient ThreadLocal<SimpleDateFormat> simpleTimeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };

    public LogPanel(String id) {
        super(id);

        final LogModel model = new LogModel();
        Form<LogModel> form = new Form<>("logForm", new CompoundPropertyModel<>(model));

        final TextField<String> keyword = addKeywordField(form);

        IModel logs = new LoadableDetachableModel<List<LogDO>>() {
            @Override
            protected List<LogDO> load() {
                return logDOs;
            }
        };

        final WebMarkupContainer container = addLogListView(logs);

        addLevelField(model, form);
        addSearchButton(model, form, keyword, container);
        addDateFields(form);

        form.setOutputMarkupId(true);
        add(form);
    }

    private WebMarkupContainer addLogListView(final IModel logs) {
        final ListView<LogDO> listView = new ListView<LogDO>("listview", logs) {
            @Override
            protected void populateItem(ListItem<LogDO> item) {
                LogDO model = item.getModelObject();
                item.add(new Label("eventid", model.getId()));
                item.add(new Label("date", toDate(model.getTimestamp())));
                item.add(new Label("time", toTime(model.getTimestamp())));
                item.add(new Label("level", model.getLevel()));
                String callerClass = model.getCallerClass();
                item.add(new Label("class", callerClass.substring(callerClass.lastIndexOf('.') + 1)));
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
        return container;
    }

    private void addSearchButton(final LogModel model, final Form<LogModel> form, final TextField<String> keyword, final WebMarkupContainer container) {
        AjaxFallbackButton searchButton = new AjaxFallbackButton("submit", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                handleSubmit(model);
                target.add(container);
                target.focusComponent(keyword);
            }
        };
        form.add(searchButton);
    }

    private void addLevelField(LogModel model, Form<LogModel> form) {
        DropDownChoice<Level> level = new DropDownChoice<>("level", availableLevel);
        model.setLevel(Level.ALL);
        form.add(level);
    }

    private void addDateFields(Form<LogModel> form) {
        final TextField<String> startTime = new TextField<>("startTime");
        startTime.setOutputMarkupId(true);
        form.add(startTime);

        final TextField<String> endTime = new TextField<>("endTime");
        endTime.setOutputMarkupId(true);
        form.add(endTime);
    }

    private TextField<String> addKeywordField(Form<LogModel> form) {
        final TextField<String> keyword = new TextField<>("keyword");
        keyword.setOutputMarkupId(true);
        form.add(keyword);
        return keyword;
    }

    public void handleSubmit(LogModel model) {
        logDOs = LogDao.get().getLogByFilter(modelToLogFilter(model));
    }

    private LogFilter modelToLogFilter(LogModel model) {
        LogFilter logFilter = LogFilter.start();
        logFilter.addKeyword(model.getKeyword());
        logFilter.addLevel(model.getLevel());
        logFilter.addStartTime(model.getStartTime());
        logFilter.addEndTime(model.getEndTime());
        logFilter.build();
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

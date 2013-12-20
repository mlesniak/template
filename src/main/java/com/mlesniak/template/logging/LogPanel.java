package com.mlesniak.template.logging;

import ch.qos.logback.classic.Level;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class LogPanel extends Panel {
    private Logger log = LoggerFactory.getLogger(LogPanel.class);
    private static List<Level> availableLevel = availableLogLevels();

    public LogPanel(String id) {
        super(id);

        final LogModel model = new LogModel();
        Form<LogModel> form = new Form<LogModel>("logForm", new CompoundPropertyModel<>(model)) {
            @Override
            protected void onSubmit() {
                handleSubmit(model);
            }
        };

        TextField<String> keyword = new TextField<>("keyword");
        form.add(keyword);

        DropDownChoice<Level> level = new DropDownChoice<>("level", availableLevel);
        model.setLevel(Level.ALL);
        form.add(level);

        add(form);
    }


    public void handleSubmit(LogModel model) {
        List<LogDO> logDOs = LogDao.get().getLogByFilter(modelToLogFilter(model));

        for (LogDO logDO : logDOs) {
            System.out.println(logDO.toString());
        }
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

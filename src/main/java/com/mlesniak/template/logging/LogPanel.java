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

    private static List<Level> availableLevel = new LinkedList<>();

    {
        availableLevel.add(Level.ALL);
        availableLevel.add(Level.ERROR);
        availableLevel.add(Level.WARN);
        availableLevel.add(Level.INFO);
        availableLevel.add(Level.DEBUG);
        availableLevel.add(Level.TRACE);
    }

    public LogPanel(String id) {
        super(id);

        final LogModel model = new LogModel();
        Form<LogModel> form = new Form<LogModel>("logForm", new CompoundPropertyModel<>(model)) {
            @Override
            protected void onSubmit() {
                System.out.println(model);
            }
        };

        TextField<String> keyword = new TextField<>("keyword");
        form.add(keyword);

        DropDownChoice<Level> level = new DropDownChoice<>("level", availableLevel);
        model.setLevel(Level.ALL);
        form.add(level);

        add(form);
    }
}

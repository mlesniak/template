package com.mlesniak.template.logging;

import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogPanel extends Panel {
    private Logger log = LoggerFactory.getLogger(LogPanel.class);

    public LogPanel(String id) {
        super(id);
    }
}

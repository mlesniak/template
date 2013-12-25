package com.mlesniak.template.errorpage;

import com.mlesniak.template.BasePage;
import com.mlesniak.template.navigation.NavigationBarPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class InternalErrorPage extends BasePage {
    private Logger log = LoggerFactory.getLogger(InternalErrorPage.class);

    public InternalErrorPage() {
        add(new NavigationBarPanel("navigationBar"));
        String code = UUID.randomUUID().toString().substring(0, 8);
        log.error("Unexpected error. code=" + code);
        add(new Label("errorCode", code));
    }
}

package com.mlesniak.template.app;

import com.mlesniak.template.config.Config;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WicketApplication extends WebApplication {
    private Logger log = LoggerFactory.getLogger(WicketApplication.class);

    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    @Override
    public void init() {
        super.init();
        log.info("Starting application.");
        Config.get().init();

        mountPage("/config", ConfigPage.class);
        mountPage("/log", LogPage.class);
    }
}

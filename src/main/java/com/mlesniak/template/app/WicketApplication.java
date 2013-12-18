package com.mlesniak.template.app;

import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

public class WicketApplication extends WebApplication {
    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    @Override
    public void init() {
        super.init();
        Config.get().init();

        mountPage("/config", ConfigPage.class);
    }
}

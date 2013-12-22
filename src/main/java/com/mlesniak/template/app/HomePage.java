package com.mlesniak.template.app;

import ch.qos.logback.classic.Level;
import com.mlesniak.template.config.Config;
import com.mlesniak.template.dao.MessageDao;
import com.mlesniak.template.logging.LogDO;
import com.mlesniak.template.logging.LogDao;
import com.mlesniak.template.logging.LogFilter;
import com.mlesniak.template.model.Message;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HomePage extends WebPage {
    private Logger log = LoggerFactory.getLogger(HomePage.class);

    public HomePage(final PageParameters parameters) {
        super(parameters);

        // For I18N-testing.
        // getSession().setLocale(Locale.GERMANY);

        addMessageInputField();
        addNotVisibleLabel();

        // Pure debugging:
        LogFilter logFilter = LogFilter.start().addLevel(Level.INFO);
        List<LogDO> logByFilter = LogDao.get().getLogByFilter(logFilter);
        for (LogDO logDo : logByFilter) {
            System.out.println(logDo);
        }
    }

    private void addNotVisibleLabel() {
        Label disabledMessage = new Label("disabledMessage", new ResourceModel("form.disabled")) {
            @Override
            public boolean isVisible() {
                return !Config.get().getBoolean(Config.Key.allowSubmit);
            }
        };
        add(disabledMessage);
    }

    private void addMessageInputField() {
        final Message message = new Message();
        Form<Message> messageForm = new Form<Message>("messageForm", new CompoundPropertyModel<>(message)) {
            @Override
            protected void onSubmit() {
                // Copy message to new object.
                Message messageDO = new Message(message);
                MessageDao.get().write(messageDO);
                log.info(message.toString(), messageDO.getId());
            }

            @Override
            public boolean isVisible() {
                return Config.get().getBoolean(Config.Key.allowSubmit);
            }
        };
        TextField<String> input = new TextField<>("message");
        add(messageForm);
        messageForm.add(input);
    }
}

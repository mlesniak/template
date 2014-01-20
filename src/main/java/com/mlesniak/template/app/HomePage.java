package com.mlesniak.template.app;

import com.mlesniak.template.BasePage;
import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigKeys;
import com.mlesniak.template.dao.MessageDao;
import com.mlesniak.template.model.MessageDO;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends BasePage {
    private Logger log = LoggerFactory.getLogger(HomePage.class);

    public HomePage(final PageParameters parameters) {
        super(parameters);

        addMessageInputField();
        addNotVisibleLabel();
    }

    private void addNotVisibleLabel() {
        Label disabledMessage = new Label("disabledMessage", new ResourceModel("form.disabled")) {
            @Override
            public boolean isVisible() {
                return !Config.get().getBoolean(ConfigKeys.ALLOW_SUBMIT);
            }
        };
        add(disabledMessage);
    }

    private void addMessageInputField() {
        final MessageDO message = new MessageDO();
        Form<MessageDO> messageForm = new Form<MessageDO>("messageForm", new CompoundPropertyModel<>(message)) {
            @Override
            protected void onSubmit() {
                MessageDO messageDO = new MessageDO(message);
                MessageDao.get().write(messageDO);
                log.info(message.toString(), messageDO.getId());
            }

            @Override
            public boolean isVisible() {
                return Config.get().getBoolean(ConfigKeys.ALLOW_SUBMIT);
            }
        };
        TextField<String> input = new TextField<>("message");
        add(messageForm);
        messageForm.add(input);
    }
}

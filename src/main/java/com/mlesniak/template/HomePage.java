package com.mlesniak.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends WebPage {
    private Logger log = LoggerFactory.getLogger(HomePage.class);

    public HomePage(final PageParameters parameters) {
        super(parameters);

        // For I18N-testing.
        // getSession().setLocale(Locale.GERMANY);

        final Message message = new Message();
        Form<Message> messageForm = new Form<Message>("messageForm", new CompoundPropertyModel<>(message)) {
            @Override
            protected void onSubmit() {
                log.info(message.toString());
            }
        };
        TextField<String> input = new TextField<>("message");
        add(messageForm);
        messageForm.add(input);
        Label disabledMessage = new Label("disabledMessage", new ResourceModel("form.disabled"));
        disabledMessage.setVisible(false);
        if (!Config.get().getBoolean(Config.Key.allowSubmit)) {
            messageForm.setVisible(false);
            disabledMessage.setVisible(true);
        }
        add(disabledMessage);
    }
}

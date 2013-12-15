package com.mlesniak.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HomePage extends WebPage {
    public HomePage(final PageParameters parameters) {
        super(parameters);

        final Message message = new Message();

        Form<Message> messageForm = new Form<Message>("messageForm", new CompoundPropertyModel<>(message)) {
            @Override
            protected void onSubmit() {
                System.out.println(message);
            }
        };
        TextField<String> input = new TextField<>("message");

        add(messageForm);
        messageForm.add(input);
    }
}

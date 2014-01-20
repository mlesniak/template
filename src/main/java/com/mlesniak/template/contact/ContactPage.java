package com.mlesniak.template.contact;

import com.mlesniak.template.BasePage;
import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigKeys;
import com.mlesniak.template.email.EmailService;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class ContactPage  extends BasePage {
    private Logger log = LoggerFactory.getLogger(ContactPage.class);

    private class ContactModel implements Serializable {
        public String name;
        public String email;
        public String message;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final ContactModel model = new ContactModel();
        Form<ContactModel> form = new Form<ContactModel>("form", new CompoundPropertyModel<>(model)) {
            @Override
            protected void onSubmit() {
                String[] args = new String[] {model.name, model.email, model.message};
                log.info("Sending contact message. from=" + model.email, args);
                EmailService.get().sendEmail(Config.get().get(ConfigKeys.ADMIN_EMAIL), "Contact", toMessage(model));
                setResponsePage(ContactThankYouPage.class);
            }
        };

        form.add(new TextField<String>("name"));
        form.add(new TextField<String>("email"));
        form.add(new TextArea<>("message"));

        add(form);
    }

    private String toMessage(ContactModel model) {
        StringBuffer sb = new StringBuffer();
        sb.append(model.name);
        sb.append("\n");
        sb.append(model.email);
        sb.append("\n");
        sb.append(model.message);
        return sb.toString();
    }
}

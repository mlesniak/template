package com.mlesniak.template.auth;

import com.mlesniak.template.app.HomePage;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

public class SignInPanel extends Panel {
    public SignInPanel(String id) {
        super(id);

        final SignInModel model = new SignInModel();
        Form<SignInModel> form = new Form<SignInModel>("signInForm", new CompoundPropertyModel<>(model)) {
            @Override
            protected void onSubmit() {
                if (model.getUsername() == null || model.getUsername().isEmpty()) {
                    return;
                }

                boolean authResult = AuthenticatedWebSession.get().signIn(model.getUsername(), model.getPassword());
                if (authResult) {
                    continueToOriginalDestination();
                    setResponsePage(HomePage.class);
                }
            }
        };

        addUsernameField(form);
        addPasswordField(form);
        add(form);
    }

    private void addUsernameField(Form<SignInModel> form) {
        final TextField<String> field = new TextField<>("username");
        field.setOutputMarkupId(true);
        form.add(field);
    }

    private void addPasswordField(Form<SignInModel> form) {
        final PasswordTextField field = new PasswordTextField("password");
        field.setOutputMarkupId(true);
        form.add(field);
    }
}

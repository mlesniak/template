package com.mlesniak.template.app;

import com.mlesniak.template.auth.SignInPanel;
import org.apache.wicket.markup.html.WebPage;

public class SignInPage extends WebPage {
    public SignInPage() {
        add(new SignInPanel("signInPanel"));
    }
}

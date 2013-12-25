package com.mlesniak.template.app;

import com.mlesniak.template.auth.SignInPanel;

public class SignInPage extends BasePage {
    public SignInPage() {
        add(new SignInPanel("signInPanel"));
    }
}

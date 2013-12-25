package com.mlesniak.template.authentification;

import com.mlesniak.template.BasePage;

public class SignInPage extends BasePage {
    public SignInPage() {
        add(new SignInPanel("signInPanel"));
    }
}

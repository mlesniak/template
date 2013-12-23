package com.mlesniak.template.app;

import com.mlesniak.template.auth.SignInPanel;
import com.mlesniak.template.navbar.NavigationBarPanel;

public class SignInPage extends BasePage {
    public SignInPage() {
        add(new SignInPanel("signInPanel"));
        add(new NavigationBarPanel("navigationBar"));
    }
}

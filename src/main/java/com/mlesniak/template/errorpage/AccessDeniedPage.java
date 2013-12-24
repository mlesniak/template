package com.mlesniak.template.errorpage;

import com.mlesniak.template.app.BasePage;
import com.mlesniak.template.navbar.NavigationBarPanel;

public class AccessDeniedPage extends BasePage {
    public AccessDeniedPage() {
        add(new NavigationBarPanel("navigationBar"));
    }
}

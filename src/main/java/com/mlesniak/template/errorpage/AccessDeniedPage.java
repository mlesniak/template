package com.mlesniak.template.errorpage;

import com.mlesniak.template.BasePage;
import com.mlesniak.template.navigation.NavigationBarPanel;

public class AccessDeniedPage extends BasePage {
    public AccessDeniedPage() {
        add(new NavigationBarPanel("navigationBar"));
    }
}

package com.mlesniak.template.logging;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class VisibleLabel extends Label {
    public VisibleLabel(String id, String label) {
        super(id, label);
    }

    public VisibleLabel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    public boolean isVisible() {
        return !(StringUtils.isEmpty(getDefaultModelObjectAsString()));
    }
}

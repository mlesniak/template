package com.mlesniak.template.plugin;

import com.mlesniak.template.config.Config;
import com.mlesniak.template.model.PluginDO;
import com.mlesniak.utils.DateUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PluginPanel extends Panel {
    private Logger log = LoggerFactory.getLogger(PluginPanel.class);

    public PluginPanel(String id) {
        super(id);

        LoadableDetachableModel<List<PluginDO>> model = new LoadableDetachableModel<List<PluginDO>>() {
            @Override
            protected List<PluginDO> load() {
                List<PluginDO> plugins = PluginService.get().getPlugins();

                Set<String> pluginsInDatabase = new HashSet<>();
                for (PluginDO plugin : plugins) {
                    pluginsInDatabase.add(plugin.getName());
                }

                // Add plugins not yet in the database.
                for (String key : Config.get().getKeys()) {
                    if (!key.startsWith("plugin.")) {
                        continue;
                    }
                    String woPlugin = key.substring("plugin.".length());
                    key = woPlugin.substring(0, woPlugin.indexOf("."));

                    if (pluginsInDatabase.contains(key)) {
                        continue;
                    }
                    pluginsInDatabase.add(key);

                    PluginDO pluginDO = new PluginDO();
                    pluginDO.setName(key);
                    plugins.add(pluginDO);

                }

                return plugins;
            }
        };

        ListView<PluginDO> listView = new ListView<PluginDO>("rows", model) {
            @Override
            protected void populateItem(ListItem<PluginDO> row) {
                PluginDO pluginDO = row.getModelObject();
                row.add(new Label("date", getDateLabel(pluginDO)));
                row.add(new Label("time", getTimeLabel(pluginDO)));
                row.add(new Label("name", pluginDO.getName()));
            }

            private String getTimeLabel(PluginDO pluginDO) {
                if (pluginDO.getJar() == null) {
                    return "-";
                }
                return DateUtils.toTime(pluginDO.getTimestamp());
            }

            private String getDateLabel(PluginDO pluginDO) {
                if (pluginDO.getJar() == null) {
                    return "-";
                }
                return DateUtils.toDate(pluginDO.getTimestamp());
            }
        };
        add(listView);

        final IModel<String> pluginChoice = new Model<String>() {
            String pluginDO;

            @Override
            public String getObject() {
                return pluginDO;
            }

            @Override
            public void setObject(String object) {
                pluginDO = object;
            }
        };

        final List<String> choices = new ArrayList<>();
        for (PluginDO pluginDO : model.getObject()) {
            choices.add(pluginDO.getName());
        }
        final DropDownChoice<String> plugin = new DropDownChoice<>("plugin", pluginChoice, choices);

        FileUploadField uploadField = new FileUploadField("fileUpload");

        Form<Void> form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                System.out.println(pluginChoice.getObject());
            }
        };
        form.add(plugin);
        form.add(uploadField);
        add(form);
    }
}

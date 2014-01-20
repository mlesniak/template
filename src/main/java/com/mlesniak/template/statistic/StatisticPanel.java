package com.mlesniak.template.statistic;

import com.mlesniak.template.dao.StatisticDao;
import com.mlesniak.template.model.StatisticDO;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticPanel extends Panel implements Serializable {
    private Logger log = LoggerFactory.getLogger(StatisticPanel.class);
    private static List<StatisticCategory> availableCategories = availableCategories();
    private List<StatisticDO> statDOs = new LinkedList<>();
    private Computation computation = new Computation();

    private transient ThreadLocal<SimpleDateFormat> dateTimeFormat;

    public StatisticPanel(String id) {
        super(id);

        final StatisticModel model = new StatisticModel();
        Form<StatisticModel> form = new Form<>("logForm", new CompoundPropertyModel<>(model));
        IModel logs = new LoadableDetachableModel<List<StatisticDO>>() {
            @Override
            protected List<StatisticDO> load() {
                return statDOs;
            }
        };
        final WebMarkupContainer container = addStatisticView(logs);
        addLevelField(model, form);
        final AutoCompleteTextField<String> keyword = addKeywordField(form, model);
        addSearchButton(model, form, keyword, container);
        addDateFields(form);
        form.setOutputMarkupId(true);
        add(form);
    }

    private WebMarkupContainer addStatisticView(final IModel logs) {
        final WebMarkupContainer container = new WebMarkupContainer("view");

        container.add(new Label("min", new Model<Double>() {
            @Override
            public Double getObject() {
                return computation.getMin();
            }
        }));
        container.add(new Label("max", new Model<Double>() {
            @Override
            public Double getObject() {
                return computation.getMax();
            }
        }));
        container.add(new Label("average", new Model<Double>() {
            @Override
            public Double getObject() {
                return computation.getAverage();
            }
        }));

        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);
        add(container);
        return container;
    }

    private void addSearchButton(final StatisticModel model, final Form<StatisticModel> form,
                                 final TextField<String> keyword, final WebMarkupContainer container) {
        AjaxFallbackButton searchButton = new AjaxFallbackButton("submit", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                handleSubmit(model);
                target.add(container);
                target.focusComponent(keyword);

                if (statDOs.isEmpty()) {
                    target.appendJavaScript("plot([], [], []);");
                } else {
                    target.appendJavaScript("plot(" + joinStatisticValues() + "," + joinDateValues() + ","
                            + averageValue() + ");");
                }
            }
        };
        form.add(searchButton);
    }

    private String averageValue() {
        double avg = computation.getAverage();
        return "[[0," + avg + "], [" + (computation.getValues().size() - 1) + ", " + avg + "]]";
    }

    private DropDownChoice<StatisticCategory> addLevelField(final StatisticModel model, Form<StatisticModel> form) {
        final DropDownChoice<StatisticCategory> level = new DropDownChoice<>("category", availableCategories);
        level.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                model.setCategory(level.getModelObject());
            }
        });
        model.setCategory(StatisticCategory.All);
        form.add(level);
        return level;
    }

    private void addDateFields(Form<StatisticModel> form) {
        final TextField<String> startTime = new TextField<>("startTime");
        startTime.setOutputMarkupId(true);
        form.add(startTime);

        final TextField<String> endTime = new TextField<>("endTime");
        endTime.setOutputMarkupId(true);
        form.add(endTime);
    }

    private AutoCompleteTextField<String> addKeywordField(Form<StatisticModel> form,
                                                          final StatisticModel model) {
        AutoCompleteSettings settings = new AutoCompleteSettings()
                .setAdjustInputWidth(false)
                .setShowListOnEmptyInput(true)
                .setThrottleDelay(150);

        final AutoCompleteTextField<String> keyword = new AutoCompleteTextField<String>("keyword", settings) {
            @Override
            protected Iterator<String> getChoices(String input) {
                return StatisticDao.get().getAvailableDesciption(model.getCategory(), input).iterator();
            }
        };


        keyword.setOutputMarkupId(true);
        form.add(keyword);

        return keyword;
    }

    public void handleSubmit(StatisticModel model) {
        statDOs = StatisticDao.get().getStatisticByFilter(modelToLogFilter(model));
        computation = new Computation(statDOs);
    }

    private String joinStatisticValues() {
        StringBuffer join = new StringBuffer();
        join.append("[");
        int i = 0;
        for (Long time : computation.getValues()) {
            join.append(",[" + i++ + ", " + time + "]");
        }

        join.append("]");
        return join.deleteCharAt(1).toString();
    }

    private String joinDateValues() {
        StringBuffer join = new StringBuffer();
        join.append("[");
        for (StatisticDO statDO : statDOs) {
            join.append("," + getJSArray(statDO));
        }

        join.append("]");
        return join.deleteCharAt(1).toString();
    }

    /**
     * JS Array for category, description and timestamp.
     *
     * @param statDO
     */
    private String getJSArray(StatisticDO statDO) {
        StringBuffer sb = new StringBuffer();

        sb.append("[\"");
        sb.append(statDO.getCategory());
        sb.append("\", \"");
        sb.append(statDO.getDescription());
        sb.append("\", \"");
        sb.append(getDateTimeFormat().format(new Date(statDO.getTimestamp())));
        sb.append("\"]");

        return sb.toString();
    }

    private StatisticFilter modelToLogFilter(StatisticModel model) {
        StatisticFilter logFilter = StatisticFilter.start();
        logFilter.addKeyword(model.getKeyword());
        logFilter.addCategory(model.getCategory());
        logFilter.addStartTime(model.getStartTime());
        logFilter.addEndTime(model.getEndTime());
        return logFilter;
    }

    private static List<StatisticCategory> availableCategories() {
        List<StatisticCategory> list = new LinkedList<>();
        list.addAll(Arrays.asList(StatisticCategory.values()));
        return list;
    }

    public SimpleDateFormat getDateTimeFormat() {
        if (dateTimeFormat == null) {
            dateTimeFormat = new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("yyyy/MM/dd HH:mm");
                }
            };
        }

        return dateTimeFormat.get();
    }
}

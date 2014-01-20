package com.mlesniak.template.pages;

import com.mlesniak.template.BasePage;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Scanner;

public class StaticPage extends BasePage {
    private Logger log = LoggerFactory.getLogger(StaticPage.class);
    public StaticPage(PageParameters parameters) {
        super(parameters);

        String page;
        if (parameters.get(0) == null || StringUtils.isBlank((parameters.get(0).toString()))) {
            page = "index";
        } else {
            page = parameters.get(0).toString();
        }

        String content = getContent(page);
        if (content == null) {
            log.warn("Unable to find page. page=" + page);
            throw new AbortWithHttpErrorCodeException(404, "Not found:" + page);
        }
        content = replaceApplicationRoot(content);

        Label html = new Label("content", content);
        html.setEscapeModelStrings(false);
        add(html);
    }

    private String replaceApplicationRoot(String content) {
        String realPath = WebApplication.get().getServletContext().getContextPath();
        return content.replaceAll("\\$\\{app\\}", realPath);
    }

    private String getContent(String page) {
        ContextRelativeResource resource = new ContextRelativeResource("/static/" + page + ".html");
        try {
            InputStream stream = resource.getCacheableResourceStream().getInputStream();
            return new Scanner(stream).useDelimiter("\\Z").next();
        } catch (ResourceStreamNotFoundException e) {
            return null;
        }
    }
}

package com.mlesniak.template.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class StatisticServletFilter implements Filter {
    private Logger log = LoggerFactory.getLogger(StatisticServletFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = null;
        if (request instanceof HttpServletRequest) {
            url = ((HttpServletRequest)request).getRequestURL().toString();
        }

        long duration, starttime = System.currentTimeMillis();

        // proceed along the chain
        chain.doFilter(request, response);

        // after response returns, calculate duration and log it
        duration = System.currentTimeMillis() - starttime;
        System.out.println(url + ", " + duration);

        // TODO Use Dao?
    }

    @Override
    public void destroy() {

    }
}

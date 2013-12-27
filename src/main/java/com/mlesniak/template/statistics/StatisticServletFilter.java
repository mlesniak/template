package com.mlesniak.template.statistics;

import com.mlesniak.template.WicketApplication;
import com.mlesniak.template.dao.StatisticsDao;
import com.mlesniak.template.model.StatisticDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;

public class StatisticServletFilter implements Filter {
    private Logger log = LoggerFactory.getLogger(StatisticServletFilter.class);
    private Set<String> paths;
    // A request must be longer than this to be logged.
    private int LOG_TRESHOLD_MIN = 5;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        paths = WicketApplication.getPageMapping().keySet();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        String uri = ((HttpServletRequest) request).getRequestURI().toString();
        boolean doLog = false;
        String loggedPath = null;
        for (String path : paths) {
            if (uri.endsWith(path)) {
                doLog = true;
                loggedPath = path;
                break;
            }
        }
        // If we have two slashes and one is at the end, the hompage has been requested.
        if (isHomepage(uri)) {
            doLog = true;
            loggedPath = "/";
        }

        if (!doLog) {
            chain.doFilter(request, response);
            return;
        }

        long duration, starttime = System.currentTimeMillis();

        // proceed along the chain
        chain.doFilter(request, response);

        // after response returns, calculate duration and log it
        duration = System.currentTimeMillis() - starttime;
        if (duration < LOG_TRESHOLD_MIN) {
            return;
        }

        StatisticDO statisticDO = new StatisticDO();
        statisticDO.setCategory(StatisticCategory.Web);
        statisticDO.setDescription(loggedPath);
        statisticDO.setTime(duration);
        statisticDO.setTimestamp(starttime);
        StatisticsDao.get().write(statisticDO);
    }

    private boolean isHomepage(String uri) {
        return countSlash(uri) == 2 && uri.endsWith("/");
    }

    @Override
    public void destroy() {

    }

    private int countSlash(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '/') {
                count++;
            }
        }

        return count;
    }
}

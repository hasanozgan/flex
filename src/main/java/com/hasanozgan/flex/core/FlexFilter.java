package com.hasanozgan.flex.core;

import com.hasanozgan.flex.core.utils.FlexUrlResolver;
import com.hasanozgan.flex.core.utils.URLData;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hasanozgan on 14/07/14.
 */
public class FlexFilter implements Filter {
    private FlexUrlResolver flexUrlResolver;
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.flexUrlResolver = new FlexUrlResolver(filterConfig.getInitParameter("controller-scan"));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FlexActionFactory flexActionFactory = new FlexActionFactory((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterConfig);
        URLData urlData = flexUrlResolver.getUrlDataForUrl(flexActionFactory.getRequestUri(), flexActionFactory.getRequestMethod());

        flexActionFactory.invoke(urlData);
    }

    @Override
    public void destroy() {

    }
}

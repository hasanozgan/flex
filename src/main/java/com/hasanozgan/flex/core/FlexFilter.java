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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.flexUrlResolver = new FlexUrlResolver(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FlexAction flexAction = flexUrlResolver.actionFactory(servletRequest, servletResponse);
        flexAction.invoke();
    }

    @Override
    public void destroy() {

    }
}

package com.hasanozgan.flex;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by hasanozgan on 14/07/14.
 */
public class FlexFilter implements Filter {
    private String apiRoot;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.apiRoot = filterConfig.getInitParameter("api-root");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        servletResponse.getWriter().write(this.apiRoot + "---"+request.getRequestURI());
    }

    @Override
    public void destroy() {

    }
}

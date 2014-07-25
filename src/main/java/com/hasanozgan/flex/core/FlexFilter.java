package com.hasanozgan.flex.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasanozgan.flex.core.annotations.Secured;
import com.hasanozgan.flex.core.annotations.Path;
import com.hasanozgan.flex.core.models.request.HttpContext;
import com.hasanozgan.flex.core.models.request.HttpContextEntity;
import com.hasanozgan.flex.core.models.request.HttpContextObject;
import com.hasanozgan.flex.core.models.response.FailureStatus;
import com.hasanozgan.flex.core.models.response.Result;
import com.hasanozgan.flex.core.models.response.Results;
import com.hasanozgan.flex.core.utils.ResourceData;
import com.hasanozgan.flex.core.utils.URLData;
import com.hasanozgan.flex.core.utils.URLResolver;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by hasanozgan on 14/07/14.
 */
public class FlexFilter implements Filter {
    private URLResolver urlResolver;
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.urlResolver = new URLResolver(filterConfig.getInitParameter("action-scan"));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FlexActionFactory flexActionFactory = new FlexActionFactory((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterConfig);
        URLData urlData = urlResolver.getUrlDataForUrl(flexActionFactory.getRequestUri(), flexActionFactory.getRequestMethod());

        flexActionFactory.invoke(urlData);
    }

    @Override
    public void destroy() {

    }
}

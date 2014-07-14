package com.hasanozgan.flex;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

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

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage("com.hasanozgan.flex.examples"))
                .setScanners(new MethodAnnotationsScanner()));

        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(Resource.class);

        for (Method method : methodSet) {
            Resource resource = (Resource)method.getAnnotation(Resource.class);
            System.out.print(resource.path());
        }



        HttpServletRequest request = (HttpServletRequest)servletRequest;
        servletResponse.getWriter().write(this.apiRoot + "---"+request.getRequestURI());
    }

    @Override
    public void destroy() {

    }
}

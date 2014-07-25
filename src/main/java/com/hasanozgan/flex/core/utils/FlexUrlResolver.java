package com.hasanozgan.flex.core.utils;

import com.hasanozgan.flex.core.FlexAction;
import com.hasanozgan.flex.core.HttpMethod;
import com.hasanozgan.flex.core.annotations.Path;
import com.hasanozgan.flex.core.annotations.Secured;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class FlexUrlResolver {

    private final Set<ActionData> routes;
    private final FilterConfig filterConfig;
    private final String apiRoot;

    public FlexUrlResolver(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.apiRoot = filterConfig.getInitParameter("api-root");
        this.routes = getActionContext(filterConfig.getInitParameter("controller-scan"));
    }

    public Set<ActionData> getRoutes() {
        return this.routes;
    }

    public FlexAction actionFactory(ServletRequest servletRequest, ServletResponse servletResponse) {
        return actionFactory((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
    }

    public FlexAction actionFactory(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI().substring(request.getContextPath().length()).substring(apiRoot.length());
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        URLData urlData = getUrlDataFromRequest(uri, httpMethod);

        return new FlexAction(request, response, urlData, filterConfig);
    }

    private URLData getUrlDataFromRequest(String url, HttpMethod httpMethod) {
        URLData urlData = null;

        //Get all route keys for this plugin
        for (ActionData actionData : getRoutes()) {
            String urlToFind = url;
            String urlPattern = actionData.getPath();
            boolean urlMatch = false;
            Map<String, String> propertyMap = new HashMap<String, String>();

            if (urlToFind.contains("?")) {
                urlToFind = url.substring(0, url.indexOf("?"));
            }

            if (actionData.getHttpMethod().equals(httpMethod)) {
                if (urlPattern.equals(urlToFind)) {
                    //Excact match
                    urlMatch = true;
                } else if (urlPattern.contains("{") && urlPattern.contains("}")) {
                    urlMatch = true;
                    //If route contains dynamic parts
                    String[] urlParts = url.split("/");       //The parts of the actual URL,split by /
                    String[] currUrlParts = urlPattern.split("/");  //The parts of the URL pattern, split by /

                    //A match needs the same amount of parts
                    if (urlParts.length == currUrlParts.length) {
                        URLData tempUrlData = null;
                        for (int i = 0; i < currUrlParts.length; i++) {
                            //If current part is a parameter/dynamic part, extract it an add it to the parameter map
                            if (currUrlParts[i].startsWith("{") && currUrlParts[i].endsWith("}")) { //this is a parameter, not part of the URL match
                                propertyMap.put(currUrlParts[i].substring(1, currUrlParts[i].length() - 1), urlParts[i]);
                            } else if (!currUrlParts[i].equals(urlParts[i])) {
                                //If the current non parameter/dynamic part does not match, the URL do not match
                                urlMatch = false;
                            }
                        }
                    } else {
                        urlMatch = false;
                    }
                }
            }

            //If Url match, build up a URL data object
            if (urlMatch) {
                urlData = new URLData(urlPattern, url, propertyMap, actionData);
                break;
            }
        }

        return urlData;
    }

    private Set<ActionData> getActionContext(String controllerPackageScan) {
        Set<ActionData> resourceContext = new HashSet<ActionData>();

        if (controllerPackageScan.isEmpty()) return resourceContext;

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(controllerPackageScan))
                .setScanners(new MethodAnnotationsScanner()));

        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(Path.class);

        for (Method method : methodSet) {
            Secured authenticated = (Secured)method.getAnnotation(Secured.class);
            Path path = (Path)method.getAnnotation(Path.class);

            if (null != path) {
                resourceContext.add(new ActionData(path.uri(), path.method(), authenticated != null, method));
            }
        }

        return resourceContext;
    }
}

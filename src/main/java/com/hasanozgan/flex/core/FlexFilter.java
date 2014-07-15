package com.hasanozgan.flex.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasanozgan.flex.core.annotations.Authenticated;
import com.hasanozgan.flex.core.annotations.Resource;
import com.hasanozgan.flex.core.models.request.RequestContext;
import com.hasanozgan.flex.core.models.request.RequestContextWith;
import com.hasanozgan.flex.core.models.response.FailureStatus;
import com.hasanozgan.flex.core.models.response.Result;
import com.hasanozgan.flex.core.models.response.ResultWith;
import com.hasanozgan.flex.core.utils.ResourceData;
import com.hasanozgan.flex.core.utils.URLData;
import com.hasanozgan.flex.core.utils.URLResolver;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by hasanozgan on 14/07/14.
 */
public class FlexFilter implements Filter {
    private String apiRoot;
    private String resourcePackage;
    private String authenticatorClass;
    private Authenticator authenticator;
    private TreeMap<String, ResourceData> routes;
    private URLResolver urlResolver;
    private HttpServletRequest request;
    private HttpServletResponse response;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.apiRoot = filterConfig.getInitParameter("api-root");
        this.resourcePackage = filterConfig.getInitParameter("resource-package");
        this.authenticatorClass = filterConfig.getInitParameter("authenticator");

        this.routes = getResourceContext();
        this.urlResolver = new URLResolver(this.routes);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        this.request = (HttpServletRequest) servletRequest;
        this.response = (HttpServletResponse) servletResponse;
        prepareAuthenticator();

        String pathInfo = request.getRequestURI().substring(request.getContextPath().length()).substring(apiRoot.length());
        URLData urlData = urlResolver.getUrlDataForUrl(pathInfo, HttpMethod.valueOf(request.getMethod()));
        ResultWith result = null;

        // Resource not found
        if (null == urlData) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // action invocation...
        if (urlData.getResourceData().authenticationRequired() && !authenticator.isAuthenticated()) {
            result = Result.error(FailureStatus.AUTHENTICATION_REQUIRED);
        }
        else {
            String entity = toString(request.getInputStream());
            String path = urlData.getRealUrl();
            Map<String, String> parameters = urlData.getParameters();
            Type[] types = urlData.getResourceData().getActionMethod().getGenericParameterTypes();

            // TODO
            //Class persistentClass = (Class) ((ParameterizedType) types[0]).getActualTypeArguments()[0];

            // TODO Generated RequestContext
            try {
                Object obj = types[0].getClass().newInstance();
                Object res = urlData.getResourceData().getActionMethod().invoke(null, obj);
                result = convertInstanceOfObject(res, ResultWith.class);
            } catch (IllegalAccessException e) {
                result = Result.error(FailureStatus.UNKNOWN_ERROR);
            } catch (InvocationTargetException e) {
                result = Result.error(FailureStatus.UNKNOWN_ERROR);
            } catch (InstantiationException e) {
                result = Result.error(FailureStatus.UNKNOWN_ERROR);
            }
            servletResponse.getWriter().write("OK");

        }

        renderResult(result);
    }


    private static String toString(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    private void renderResult(ResultWith result) throws IOException {
        GsonBuilder builder = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting();
        Gson gson = builder.create();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(result.getStatus().getHttpStatus());
        Object data = result.isSucceed() ? result.getEntity() : result.getStatus();
        String json = gson.toJson(data);

        response.getWriter().write(addJSONP(json));
    }

    private String addJSONP(String json) {
        String callback = request.getParameter("callback");

        if (callback != null) {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept");

            return String.format("%s(%s)", callback, json);
        }

        return json;
    }

    private void prepareAuthenticator() {
        try {
            Class clazz = Class.forName(authenticatorClass);
            this.authenticator = (Authenticator) clazz.newInstance();
        } catch (NullPointerException e) {
            this.authenticator = new DefaultAuthenticator();
        } catch (ClassNotFoundException e) {
            this.authenticator = new DefaultAuthenticator();
        } catch (InstantiationException e) {
            this.authenticator = new DefaultAuthenticator();
        } catch (IllegalAccessException e) {
            this.authenticator = new DefaultAuthenticator();
        }

        this.authenticator.init(request);
    }

    public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return clazz.cast(o);
        } catch(ClassCastException e) {
            return null;
        }
    }

    private TreeMap<String, ResourceData> getResourceContext() {
        TreeMap<String, ResourceData> resourceContext = new TreeMap<String, ResourceData>();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(resourcePackage))
                .setScanners(new MethodAnnotationsScanner()));

        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(Resource.class);

        for (Method method : methodSet) {
            Authenticated authenticated = (Authenticated)method.getAnnotation(Authenticated.class);
            Resource resource = (Resource)method.getAnnotation(Resource.class);

            if (null != resource) {
                resourceContext.put(resource.path(), new ResourceData(resource.path(), resource.method(), authenticated != null, method));
            }
        }

        return resourceContext;
    }

    @Override
    public void destroy() {

    }
}

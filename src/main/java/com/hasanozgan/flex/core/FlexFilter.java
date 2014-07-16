package com.hasanozgan.flex.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasanozgan.flex.core.annotations.Authenticated;
import com.hasanozgan.flex.core.annotations.Resource;
import com.hasanozgan.flex.core.models.request.RequestContext;
import com.hasanozgan.flex.core.models.request.RequestContextWith;
import com.hasanozgan.flex.core.models.request.RequestContextWithEntity;
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
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

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
import java.util.*;

/**
 * Created by hasanozgan on 14/07/14.
 */
public class FlexFilter implements Filter {
    private String apiRoot;
    private String resourcePackage;
    private String authenticatorClass;
    private Authenticator authenticator;
    private Set<ResourceData> routes;
    private URLResolver urlResolver;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Gson gson;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.apiRoot = filterConfig.getInitParameter("api-root");
        this.resourcePackage = filterConfig.getInitParameter("resource-package");
        this.authenticatorClass = filterConfig.getInitParameter("authenticator");

        this.routes = getResourceContext();
        this.urlResolver = new URLResolver(this.routes);

        GsonBuilder builder = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting();
        gson = builder.create();
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
            if (request.getHeader("Accept").contains("application/json")) {
                renderResult(Result.error(FailureStatus.NOT_FOUND));
            }
            else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
            return;
        }

        // action invocation...
        if (urlData.getResourceData().authenticationRequired() && !authenticator.isAuthenticated()) {
            result = Result.error(FailureStatus.AUTHENTICATION_REQUIRED);
        }
        else {
            result = actionInvoker(urlData);
        }

        renderResult(result);
    }

    private ResultWith actionInvoker(URLData urlData) {
        if (urlData == null || urlData.getResourceData() == null || urlData.getResourceData().getActionMethod() == null) {
            return Result.error(FailureStatus.INVALID_URL_DATA_CONFIGURATION);
        }

        ResultWith result = null;

        result = createActionMethodParameter(urlData);
        if (!result.isSucceed()) {
            return result;
        }
        Object parameter = result.getEntity();
        try {
            result = (ResultWith) ((parameter == null)
                    ? urlData.getResourceData().getActionMethod().invoke(null)
                    : urlData.getResourceData().getActionMethod().invoke(null, parameter));
        } catch (Exception e) {
            Result.error(FailureStatus.INVOKER_ERROR);
        }

        return result;
    }

    private String fetchEntityString() {
        try {
            return toString(request.getInputStream());
        } catch (IOException e) {
            return null;
        }
    }

    private ResultWith createActionMethodParameter(URLData urlData) {
        Type[] types = urlData.getResourceData().getActionMethod().getGenericParameterTypes();

        if (types.length == 0) return Result.ok();

        if (RequestContext.class.equals(types[0])) {
            return Result.ok(new RequestContext(request, response, authenticator, urlData.getParameters()));
        }
        else if (types[0] instanceof ParameterizedTypeImpl) {
            ParameterizedTypeImpl parameterizedType = ((ParameterizedTypeImpl)types[0]);

            // RequestContextWith Degilse hatayı çak
            if (!RequestContextWith.class.equals(parameterizedType.getRawType()))
                return Result.error(FailureStatus.INVALID_ACTION_METHOD_PARAMETERS);

            String jsonEntity = fetchEntityString();
            Object entity = null;
            if (!jsonEntity.isEmpty()) {
                entity = gson.fromJson(jsonEntity, parameterizedType.getActualTypeArguments()[0]);
            }

            return Result.ok(new RequestContextWithEntity(request, response, authenticator, entity, urlData.getParameters()) {});
        }
        else {
            return Result.error(FailureStatus.INVALID_ACTION_METHOD_PARAMETERS);
        }
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

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(result.getStatus().getHttpStatus());
        Object data = result.isSucceed() ? result.getEntity() : result.getStatus();

        response.getWriter().write(addJSONP((data != null) ? gson.toJson(data) : "{}"));
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

    private Set<ResourceData> getResourceContext() {
        Set<ResourceData> resourceContext = new HashSet<ResourceData>();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(resourcePackage))
                .setScanners(new MethodAnnotationsScanner()));

        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(Resource.class);

        for (Method method : methodSet) {
            Authenticated authenticated = (Authenticated)method.getAnnotation(Authenticated.class);
            Resource resource = (Resource)method.getAnnotation(Resource.class);

            if (null != resource) {
                resourceContext.add(new ResourceData(resource.path(), resource.method(), authenticated != null, method));
            }
        }

        return resourceContext;
    }

    @Override
    public void destroy() {

    }
}

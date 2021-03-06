package com.hasanozgan.flex.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasanozgan.flex.core.models.request.HttpContext;
import com.hasanozgan.flex.core.models.request.HttpContextEntity;
import com.hasanozgan.flex.core.models.request.HttpContextObject;
import com.hasanozgan.flex.core.models.response.FailureStatus;
import com.hasanozgan.flex.core.models.response.Result;
import com.hasanozgan.flex.core.models.response.Results;
import com.hasanozgan.flex.core.utils.URLData;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Created by hasanozgan on 25/07/14.
 */
public class FlexAction {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final URLData urlData;
    private final Authenticator authenticator;
    private final ServletContext servletContext;
    private final GsonBuilder builder;


    public FlexAction(HttpServletRequest request, HttpServletResponse response, URLData urlData, FilterConfig filterConfig) {
        this.request = request;
        this.response = response;
        this.urlData = urlData;
        this.authenticator = createAuthenticator(filterConfig, request);
        this.servletContext = filterConfig.getServletContext();
        this.builder = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting();
    }

    public void invoke() throws IOException {

        Result result = null;
        if (null == urlData) {
            result = Results.error(FailureStatus.NOT_FOUND);
        }
        else if (urlData.getActionData().authenticationRequired() && !authenticator.isAuthenticated()) {
            result = Results.error(FailureStatus.AUTHENTICATION_REQUIRED);
        }
        else {
            result = methodCall(urlData);
        }

        renderResult(result);
    }




    private Result methodCall(URLData urlData) {
        if (urlData == null || urlData.getActionData() == null || urlData.getActionData().getActionMethod() == null) {
            return Results.error(FailureStatus.INVALID_URL_DATA_CONFIGURATION);
        }

        Result result = null;

        result = createActionMethodParameter(urlData);
        if (result.isFailure()) {
            return result;
        }
        Object methodArgs = result.getEntity();
        try {
            Object clazz = urlData.getActionData().getActionMethod().getDeclaringClass().newInstance();
            result = (Result) ((methodArgs == null)
                    ? urlData.getActionData().getActionMethod().invoke(clazz)
                    : urlData.getActionData().getActionMethod().invoke(clazz, methodArgs));
        } catch (Exception e) {
            result = Results.error(FailureStatus.INVOKER_ERROR);
        }

        return result;
    }

    private String getRawBody(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
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

    private String fetchEntityString() {
        try {
            return getRawBody(request.getInputStream());
        } catch (IOException e) {
            return null;
        }
    }

    private Result createActionMethodParameter(URLData urlData) {
        Type[] types = urlData.getActionData().getActionMethod().getGenericParameterTypes();

        if (types.length == 0) return Results.ok();

        if (HttpContext.class.equals(types[0])) {
            return Results.ok(new HttpContextObject(request, response, servletContext, authenticator, urlData.getParameters()));
        }
        else if (types[0] instanceof ParameterizedTypeImpl) {
            ParameterizedTypeImpl parameterizedType = ((ParameterizedTypeImpl)types[0]);

            // RequestContext tipinde değilse hata ver.
            if (!HttpContext.class.equals(parameterizedType.getRawType()))
                return Results.error(FailureStatus.INVALID_ACTION_METHOD_PARAMETERS);

            String jsonEntity = fetchEntityString();
            Object entity = null;
            if (!jsonEntity.isEmpty()) {
                try {
                    Gson gson = builder.create();
                    entity = gson.fromJson(jsonEntity, parameterizedType.getActualTypeArguments()[0]);
                    return Results.ok(new HttpContextEntity(request, response, servletContext, authenticator, entity, urlData.getParameters()) {});
                }
                catch (Exception ex) {
                }
            }
            return Results.error(FailureStatus.INVALID_JSON_DATA);
        }
        else {
            return Results.error(FailureStatus.INVALID_ACTION_METHOD_PARAMETERS);
        }
    }

    private void renderResult(Result result) throws IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(result.getStatus().getHttpStatus());

        Gson gson = builder.create();
        Object data = result.isSuccess() ? result.getEntity() : result.getStatus();
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

    private Authenticator createAuthenticator(FilterConfig filterConfig, HttpServletRequest request) {
        String authenticatorClass = filterConfig.getInitParameter("authenticator");
        Authenticator authenticator = null;
        try {
            Class clazz = Class.forName(authenticatorClass);
            authenticator = (Authenticator) clazz.newInstance();
        } catch (Exception e) {
            authenticator = new DefaultAuthenticator();
        }

        authenticator.init(request);

        return authenticator;
    }
}

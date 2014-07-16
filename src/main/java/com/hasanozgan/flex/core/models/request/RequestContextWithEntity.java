package com.hasanozgan.flex.core.models.request;

import com.hasanozgan.flex.core.Authenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class RequestContextWithEntity<T> implements RequestContextWith<T> {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Authenticator authenticator;
    private final T entity;
    private final Map<String, String> parameters;

    public RequestContextWithEntity(HttpServletRequest request, HttpServletResponse response, Authenticator authenticator, T entity, Map<String, String> parameters) {
        this.request = request;
        this.response = response;
        this.authenticator = authenticator;
        this.parameters = parameters;
        this.entity = entity;
    }

    @Override
    public Authenticator getAuthenticator() {
        return authenticator;
    }

    @Override
    public T getEntity() {
        return entity;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public HttpServletResponse getResponse() {
        return response;
    }

    @Override
    public String getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }
}

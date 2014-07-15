package com.hasanozgan.flex.core.models.request;

import com.hasanozgan.flex.core.Authenticator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class RequestContextWithImpl<T> implements RequestContextWith<T> {

    private final HttpServletRequest request;
    private final Authenticator authenticator;
    private final T entity;
    private final Map<String, String> parameters;

    public RequestContextWithImpl(HttpServletRequest request, Authenticator authenticator, T entity, Map<String, String> parameters) {
        this.request = request;
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
    public String getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }
}

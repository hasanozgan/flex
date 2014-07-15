package com.hasanozgan.flex.core.models.request;

import com.hasanozgan.flex.core.Authenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class RequestContext implements RequestContextWith<Object> {

    private final HttpServletRequest request;
    private final Authenticator authenticator;
    private final Map<String, String> parameters;

    public RequestContext(HttpServletRequest request, Authenticator authenticator, Map<String, String> parameters) {
        this.request = request;
        this.authenticator = authenticator;
        this.parameters = parameters;
    }

    @Override
    public Authenticator getAuthenticator() {
        return authenticator;
    }

    @Override
    public Object getEntity() {
        return new Object();
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

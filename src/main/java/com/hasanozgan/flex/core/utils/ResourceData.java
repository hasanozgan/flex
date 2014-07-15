package com.hasanozgan.flex.core.utils;

import com.hasanozgan.flex.core.HttpMethod;

import java.lang.reflect.Method;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class ResourceData {
    private final Method actionMethod;
    private final String path;
    private final HttpMethod httpMethod;
    private final boolean authenticationRequired;

    public ResourceData(String path, HttpMethod httpMethod, boolean authenticationRequired, Method actionMethod) {
        this.path = path;
        this.httpMethod = httpMethod;
        this.authenticationRequired = authenticationRequired;
        this.actionMethod = actionMethod;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public boolean authenticationRequired() {
        return authenticationRequired;
    }
}

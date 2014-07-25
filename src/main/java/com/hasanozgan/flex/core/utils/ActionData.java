package com.hasanozgan.flex.core.utils;

import com.hasanozgan.flex.core.HttpMethod;

import java.lang.reflect.Method;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class ActionData {
    private final Method actionMethod;
    private final String path;
    private final HttpMethod httpMethod;
    private final boolean authenticationRequired;

    public ActionData(String path, HttpMethod httpMethod, boolean authenticationRequired, Method actionMethod) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionData)) return false;

        ActionData that = (ActionData) o;

        if (httpMethod != that.httpMethod) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (httpMethod != null ? httpMethod.hashCode() : 0);
        return result;
    }
}

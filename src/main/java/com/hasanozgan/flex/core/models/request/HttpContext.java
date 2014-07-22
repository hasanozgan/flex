package com.hasanozgan.flex.core.models.request;

import com.hasanozgan.flex.core.Authenticator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public interface HttpContext<T> {
    public Authenticator getAuthenticator();
    public T getEntity();
    public HttpServletRequest getRequest();
    public HttpServletResponse getResponse();
    public ServletContext getServletContext();
    public String getPathParameter(String key);
    public Map<String, String> getPathParameters();
}

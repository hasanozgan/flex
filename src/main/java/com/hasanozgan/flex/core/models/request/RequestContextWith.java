package com.hasanozgan.flex.core.models.request;

import com.hasanozgan.flex.core.Authenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public interface RequestContextWith<T> {
    public Authenticator getAuthenticator();
    public T getEntity();
    public HttpServletRequest getRequest();
    public HttpServletResponse getResponse();
    public String getParameter(String key);
    public Map<String, String> getParameters();
}

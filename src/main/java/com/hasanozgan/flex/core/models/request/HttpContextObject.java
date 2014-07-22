package com.hasanozgan.flex.core.models.request;

import com.hasanozgan.flex.core.Authenticator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class HttpContextObject extends HttpContextEntity<Object> {
    public HttpContextObject(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, Authenticator authenticator, Map<String, String> parameters) {
        super(request, response, servletContext, authenticator, new Object(), parameters);
    }
}

package com.hasanozgan.flex.core.models.request;

import com.hasanozgan.flex.core.Authenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class RequestContext extends RequestContextWithEntity<Object> {
    public RequestContext(HttpServletRequest request, HttpServletResponse response, Authenticator authenticator, Map<String, String> parameters) {
        super(request, response, authenticator, new Object(), parameters);
    }
}

package com.hasanozgan.flex.core;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class DefaultAuthenticator implements Authenticator<Object> {
    private HttpServletRequest request;

    @Override
    public void init(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public Object getUser() {
        return null;
    }
}

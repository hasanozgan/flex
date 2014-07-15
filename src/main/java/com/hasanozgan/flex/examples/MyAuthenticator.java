package com.hasanozgan.flex.examples;

import com.hasanozgan.flex.core.Authenticator;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class MyAuthenticator implements Authenticator<User> {
    private HttpServletRequest request;

    @Override
    public void init(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public User getUser() {
        return new User(1, "hasan");
    }
}

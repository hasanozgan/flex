package com.hasanozgan.flex.core;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public interface Authenticator<U> {
    public void init(HttpServletRequest request);
    public boolean isAuthenticated();
    public U getUser();
}

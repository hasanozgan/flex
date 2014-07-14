package com.hasanozgan.flex.examples;

import com.hasanozgan.flex.HttpMethod;
import com.hasanozgan.flex.Resource;
import com.hasanozgan.flex.Result;

import java.util.Map;

/**
 * Created by hasanozgan on 14/07/14.
 */
public class UserResource {

    @Resource(path = "/user/info", method = HttpMethod.GET)
    public Result userInfo(Map parameters) {
        return new Result() {
        };
    }

    @Resource(path = "/user/details", method = HttpMethod.GET)
    public Result userDetails(Map parameters) {
        return new Result() {
        };
    }
}

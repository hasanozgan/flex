package com.hasanozgan.flex.examples;

import com.hasanozgan.flex.core.annotations.Authenticated;
import com.hasanozgan.flex.core.HttpMethod;
import com.hasanozgan.flex.core.annotations.Resource;
import com.hasanozgan.flex.core.models.request.RequestContext;
import com.hasanozgan.flex.core.models.request.RequestContextWith;
import com.hasanozgan.flex.core.models.response.Result;
import com.hasanozgan.flex.core.models.response.ResultWith;

/**
 * Created by hasanozgan on 14/07/14.
 */
public class UserResource {

    @Authenticated
    @Resource(path = "/user/info", method = HttpMethod.GET)
    public static ResultWith<User> userInfo(RequestContextWith<User> ctx) {
        return Result.ok(new User(1, "hasan"));
    }

    @Resource(path = "/user/details", method = HttpMethod.POST)
    public static Result userDetails(RequestContext ctx) {
        return Result.ok();
    }
}

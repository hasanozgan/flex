package com.hasanozgan.flex.examples;

import com.hasanozgan.flex.core.annotations.Secured;
import com.hasanozgan.flex.core.HttpMethod;
import com.hasanozgan.flex.core.annotations.Path;
import com.hasanozgan.flex.core.models.request.HttpContext;
import com.hasanozgan.flex.core.models.response.Result;
import com.hasanozgan.flex.core.models.response.Results;

/**
 * Created by hasanozgan on 14/07/14.
 */
public class UserResource {

    @Secured
    @Path(path = "/user/info", method = HttpMethod.GET)
    public static Result<User> getUserInfo() {
        return Results.ok(new User(1, "hasan"));
    }

    @Secured
    @Path(path = "/user/info", method = HttpMethod.POST)
    public static Result<User> postUserInfo(HttpContext<User> ctx) {
        return Results.ok(ctx.getEntity());
    }

    @Secured
    @Path(path = "/user/coupons/{gameType}", method = HttpMethod.GET)
    public static Result<String> getUserCoupons(HttpContext ctx) {
        return Results.ok(ctx.getPathParameter("gameType"));
    }

    @Path(path = "/user/details", method = HttpMethod.GET)
    public static Result getUserDetails(HttpContext ctx) {
        return Results.ok();
    }
}

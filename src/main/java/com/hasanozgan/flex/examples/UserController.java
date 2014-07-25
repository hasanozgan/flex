package com.hasanozgan.flex.examples;

import com.hasanozgan.flex.core.annotations.Secured;
import com.hasanozgan.flex.core.HttpMethod;
import com.hasanozgan.flex.core.annotations.Action;
import com.hasanozgan.flex.core.models.request.HttpContext;
import com.hasanozgan.flex.core.models.response.Result;
import com.hasanozgan.flex.core.models.response.Results;

/**
 * Created by hasanozgan on 14/07/14.
 */
public class UserController {

    @Secured
    @Action(uri = "/user/info", method = HttpMethod.GET)
    public Result<User> getUserInfo() {
        return Results.ok(new User(1, "hasan"));
    }

    @Secured
    @Action(uri = "/user/info", method = HttpMethod.POST)
    public Result<User> postUserInfo(HttpContext<User> ctx) {
        return Results.ok(ctx.getEntity());
    }

    @Secured
    @Action(uri = "/user/coupons/{gameType}", method = HttpMethod.GET)
    public Result<String> getUserCoupons(HttpContext ctx) {
        return Results.ok(ctx.getPathParameter("gameType"));
    }

    @Action(uri = "/user/details", method = HttpMethod.GET)
    public Result getUserDetails(HttpContext ctx) {
        return Results.ok();
    }
}

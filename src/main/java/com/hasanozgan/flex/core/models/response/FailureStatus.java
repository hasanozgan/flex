package com.hasanozgan.flex.core.models.response;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class FailureStatus extends Status {
    public static final Status AUTHENTICATION_REQUIRED = new FailureStatus(1000, "authentication required", 403);
    public static final Status INVALID_URL_DATA_CONFIGURATION = new FailureStatus(1010, "invalid url data configuration");
    public static final Status INVOKER_ERROR = new FailureStatus(1020, "invoker error");
    public static final Status INVALID_ACTION_METHOD_PARAMETERS = new FailureStatus(1030, "invalid action method parameters");
    public static final Status NOT_FOUND = new FailureStatus(1404, "not found", 404);
    public static final Status UNKNOWN_ERROR = new FailureStatus(9999, "unknown error");

    public FailureStatus(Integer code, String description) {
        super(code, description);
    }

    public FailureStatus(Integer code, String description, Integer httpStatus) {
        super(code, description, httpStatus);
    }


    @Override
    protected Integer getStatusGroupCode() {
        return 10000;
    }
}
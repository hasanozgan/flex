package com.hasanozgan.flex.core.models.response;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class FailureStatus extends Status {
    public final static Status AUTHENTICATION_REQUIRED = new FailureStatus(1000, "authentication required", 403);
    public final static Status UNKNOWN_ERROR = new FailureStatus(9999, "unknown error");

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
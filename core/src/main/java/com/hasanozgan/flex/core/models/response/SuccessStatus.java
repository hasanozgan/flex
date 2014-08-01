package com.hasanozgan.flex.core.models.response;

/**
 * Created with IntelliJ IDEA.
 * User: hasan.ozgan
 * Date: 7/1/14
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SuccessStatus extends Status {
    public final static Status SUCCESS = new SuccessStatus(0, "Success");

    public SuccessStatus(Integer code, String description) {
        super(code, description, 200);
    }

    @Override
    protected Integer getStatusGroupCode() {
        return 0;
    }
}

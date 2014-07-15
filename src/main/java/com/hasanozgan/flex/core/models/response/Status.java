package com.hasanozgan.flex.core.models.response;

/**
 * Created with IntelliJ IDEA.
 * User: hasan.ozgan
 * Date: 7/1/14
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Status {
    private final Integer code;
    private final String description;
    private final transient Integer httpStatus;

    protected abstract Integer getStatusGroupCode();

    public Status(Integer code, String description) {
        this(code, description, 500);
    }

    public Status(Integer code, String description, Integer httpStatus) {
        this.httpStatus = httpStatus;
        this.code = generateCode(code);
        this.description = description;
    }

    protected Integer generateCode(Integer statusCode) {
        return statusCode + getStatusGroupCode();
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }
}

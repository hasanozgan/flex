package com.hasanozgan.flex.core.models.response;

/**
 * Created with IntelliJ IDEA.
 * User: hasan.ozgan
 * Date: 7/1/14
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
class Failure extends Result {

    private final Status status;
    private final Object entity;

    public Failure(Status status, Object entity) {
        this.entity = entity;
        this.status = status;
    }

    public Failure(Status status) {
        this(status, new Object());
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public Boolean isSucceed() {
        return false;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public static Result withType(Status status) {
        return (Result) new Failure(status, new Object());
    }
}
